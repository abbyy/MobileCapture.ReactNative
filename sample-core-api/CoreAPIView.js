/// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
/// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

import React, {useState} from 'react';
import {
  View,
  Text,
  Alert,
  TouchableOpacity,
  StyleSheet,
  ScrollView,
  Modal,
} from 'react-native';
import {
  startImageCapture,
  rotateImage,
  assessQualityForOcr,
  detectDocumentBoundary,
  cropImage,
  recognizeText,
  extractData,
  exportImagesToPdf,
  exportImage,
} from 'react-native-abbyy-mobile-capture';
import SettingsView, {settings} from './SettingsView';
import Svg, {
  G,
  Text as SvgText,
  Polygon,
  Rect,
  Image as SvgImage,
} from 'react-native-svg';
import {DocumentDirectoryPath, mkdir, unlink} from 'react-native-fs';
import Share from 'react-native-share';

// Initialize result directory
const resultDirectory = DocumentDirectoryPath + '/sample-core-api';
// Delete directory recursively
unlink(resultDirectory)
  .finally(() => {
    // Create directory
    mkdir(resultDirectory).then(success => {
      console.log('Result directory was initialized successfully');
    });
  })
  .catch(error => {
    // Error is okay in case of file not found
  });

//-- Actions

const selectImage = async completion => {
  try {
    const request = {
      method: 'startImageCapture',
      settings: {
        requiredPageCount: 1,
        destination: settings.destination,
        exportType: settings.exportType,
        compressionLevel: settings.compressionLevel,
      },
    };
    const result = await startImageCapture(request.settings);
    if (result.images) {
      let imageInfo = result.images[0];
      const imageUri =
        result.resultInfo.uriPrefix +
        (imageInfo.filePath ? imageInfo.filePath : imageInfo.base64);
      completion({
        imageUri: imageUri,
        imageSize: imageInfo.resultInfo.imageSize,
        rawResult: result,
        rawRequest: request,
      });
    }
  } catch (error) {
    Alert.alert('Select Error', error.message);
  }
};

const getResultSettings = fileName => {
  return {
    destination: settings.destination,
    exportType: settings.exportType,
    filePath:
      resultDirectory +
      '/' +
      fileName +
      Date.now() + // Add timestamp to skip image caching
      '.' +
      settings.exportType.toLowerCase(),
    compressionLevel: settings.compressionLevel,
  };
};

const rotate = async (imageUri, completion) => {
  try {
    const request = {
      method: 'rotateImage',
      settings: {
        imageUri: imageUri,
        angle: settings.angle,
        result: getResultSettings('rotated_image'),
      },
    };
    const result = await rotateImage(request.settings);
    completion(request, result);
  } catch (error) {
    Alert.alert('Rotate Error', error.message);
  }
};

const assessQuality = async (imageUri, completion) => {
  try {
    const request = {
      method: 'assessQualityForOcr',
      settings: {imageUri: imageUri},
    };
    const result = await assessQualityForOcr(request.settings);
    completion(request, result);
  } catch (error) {
    Alert.alert('AssessQualityForOcr Error', error.message);
  }
};

const crop = async (imageUri, documentBoundary, completion) => {
  try {
    if (!documentBoundary) {
      const boundaryResult = await detectDocumentBoundary({
        imageUri: imageUri,
        detectionMode: settings.detectionMode,
      });
      documentBoundary = boundaryResult.documentBoundary;
    }
    if (documentBoundary) {
      const cropRequest = {
        method: 'cropResult',
        settings: {
          imageUri: imageUri,
          documentBoundary: documentBoundary,
          result: getResultSettings('cropped_image'),
        },
      };
      const cropResult = await cropImage(cropRequest.settings);
      completion(cropRequest, cropResult);
    } else {
      Alert.alert('Document boundary not found');
    }
  } catch (error) {
    Alert.alert('Crop Error', error.message);
  }
};

const shareToPdf = async (imageUri, completion) => {
  try {
    const request = {
      method: 'exportImagesToPdf',
      settings: {
        images: [
          {
            imageUri: imageUri,
            compressionLevel: settings.compressionLevel,
          },
        ],
        result: {
          destination: settings.destination,
          filePath: resultDirectory + '/file.pdf',
        },
        pdfInfo: {
          title: settings.pdfInfoTitle,
        },
      },
    };
    const result = await exportImagesToPdf(request.settings);
    await Share.open({
      title: 'Share pdf',
      url: result.pdfUri,
      showAppsToView: true,
      failOnCancel: false,
    });
    completion(request, result);
  } catch (error) {
    Alert.alert('To PDF Error', error.message);
  }
};

const recognize = async (imageUri, size, completion) => {
  try {
    const request = {
      method: 'recognizeText',
      settings: {
        imageUri: imageUri,
        recognitionLanguages: settings.languages,
        isTextOrientationDetectionEnabled:
          settings.isTextOrientationDetectionEnabled,
      },
    };
    let result = await recognizeText(request.settings);
    let text = result.text;
    let textLines = [];
    let blocks = result.textBlocks ? result.textBlocks : [];
    for (let block of blocks) {
      let lines = block.textLines ? block.textLines : [];
      for (let textLine of lines) {
        textLines.push(textLine);
      }
    }
    if (result.warnings) {
      text += '\n\nWarnings: ' + JSON.stringify(result.warnings);
    }

    let rotatedImageUri = imageUri;
    let rotatedSize = size;
    if (result.orientation !== 0) {
      const rotationResult = await rotateImage({
        imageUri: imageUri,
        angle: 360 - result.orientation,
        result: getResultSettings('rotated_after_recognition_image'),
      });
      rotatedImageUri = rotationResult.imageUri;
      rotatedSize = rotationResult.imageSize;
    }
    completion({
      text: text,
      lines: textLines,
      imageUri: rotatedImageUri,
      imageSize: rotatedSize,
      rawResult: result,
      rawRequest: request,
    });
  } catch (error) {
    Alert.alert('Recognize Error', error.message);
  }
};

const extract = async (imageUri, size, completion) => {
  try {
    const request = {
      method: '',
      settings: {
        imageUri: imageUri,
        profile: 'BusinessCards',
        recognitionLanguages: settings.languages,
        isTextOrientationDetectionEnabled:
          settings.isTextOrientationDetectionEnabled,
      },
    };
    let result = await extractData(request.settings);

    let fields = result.dataFields ? result.dataFields : [];
    let text = '';
    let lines = [];
    for (let field of fields) {
      if (field.id !== 'Text') {
        text += field.id + ': ' + field.text + '\n';
        lines.push(field);
        if (field.components) {
          for (let component of field.components) {
            if (component.id) {
              text += '\t' + component.id + ': ' + component.text + '\n';
            }
          }
        }
      }
    }
    if (result.warnings) {
      text += '\n\nWarnings: ' + JSON.stringify(result.warnings);
    }
    let rotatedImageUri = imageUri;
    let rotatedSize = size;
    if (result.orientation !== 0) {
      const rotationResult = await rotateImage({
        imageUri: imageUri,
        angle: 360 - result.orientation,
        result: getResultSettings('rotated_after_data_extraction_image'),
      });
      rotatedImageUri = rotationResult.imageUri;
      rotatedSize = rotationResult.imageSize;
    }

    completion({
      text: text,
      lines: lines,
      imageUri: rotatedImageUri,
      imageSize: rotatedSize,
      rawResult: result,
      rawRequest: request,
    });
  } catch (error) {
    Alert.alert('Extract Error', error.message);
  }
};

const shareImage = async (imageUri, completion) => {
  try {
    const request = {
      method: 'exportImage',
      settings: {
        imageUri: imageUri,
        result: getResultSettings('share_image'),
      },
    };
    const result = await exportImage(request.settings);
    console.log('After result');
    await Share.open({
      title: 'Share image',
      url: result.imageUri,
      showAppsToView: true,
      failOnCancel: false,
    });
    console.log('After share');
    completion(request, result);
  } catch (error) {
    Alert.alert('Share Image Error', error.message);
  }
};

const detectBoundary = async (imageUri, completion) => {
  try {
    const request = {
      method: 'detectDocumentBoundary',
      settings: {
        imageUri: imageUri,
        detectionMode: settings.detectionMode,
      },
    };
    const result = await detectDocumentBoundary(request.settings);
    if (!result.documentBoundary) {
      Alert.alert('Document boundary not found');
    }
    completion(request, result);
  } catch (error) {
    Alert.alert('Detect Boundary Error', error.message);
  }
};

const truncateBase64String = dict => {
  if (dict.imageUri && dict.imageUri.length > 300) {
    return {
      ...dict,
      imageUri:
        dict.imageUri.substring(0, 50) + ' ... length: ' + dict.imageUri.length,
    };
  }
  if (dict.base64) {
    return {
      ...dict,
      base64:
        dict.base64.substring(0, 50) + ' ... length: ' + dict.base64.length,
    };
  }

  if (dict.settings) {
    return {...dict, settings: truncateBase64String(dict.settings)};
  }
  if (dict.images) {
    let truncated = [];
    for (let image of dict.images) {
      truncated.push(truncateBase64String(image));
    }
    return {...dict, images: truncated};
  }
  return dict;
};

//-- UI

const updateImageViewLayout = (parentLayout, image) => {
  if (image.imageSize) {
    let imageSize = image.imageSize;
    const maxImageHeight = 320;
    let imageViewLayout = {};
    if (imageSize.width > imageSize.height) {
      imageViewLayout.width = parentLayout.width ? parentLayout.width : 320;
      imageViewLayout.height =
        (imageViewLayout.width * imageSize.height) / imageSize.width;
    } else {
      imageViewLayout.height = maxImageHeight;
      imageViewLayout.width =
        (imageViewLayout.height * imageSize.width) / imageSize.height;
    }
    return imageViewLayout;
  }
};

const pointsFromQuadrangle = (scaleX, scaleY, quadrangle) => {
  let points = [];
  for (let point of quadrangle) {
    let x = point.x * scaleX;
    let y = point.y * scaleY;
    points.push(x + ',' + y);
  }
  return points;
};

const boundaryUI = (scaleX, scaleY, boundary) => {
  let points = pointsFromQuadrangle(scaleX, scaleY, boundary);
  return <Polygon points={points.join(' ')} stroke="blue" />;
};

const recognizedTextLinesUI = (scaleX, scaleY, lines) => {
  var uiLines = [];
  let key = 0;
  lines.forEach(function(line) {
    let points = pointsFromQuadrangle(scaleX, scaleY, line.quadrangle);
    let topLeft = {x: line.rect.left * scaleX, y: line.rect.top * scaleY};

    let fieldIdFontSize = 11;
    uiLines.push(
      <G key={key}>
        <Polygon points={points.join(' ')} stroke="blue" />
        <SvgText
          x={topLeft.x}
          y={topLeft.y}
          fontWeight="bold"
          fontSize={fieldIdFontSize}
          fill="blue"
          textAnchor="start">
          {line.id}
        </SvgText>
      </G>,
    );
    key = key + 1;
  });
  return <G>{uiLines}</G>;
};

const assessQualityForOcrUIBlocks = (scaleX, scaleY, blocks) => {
  var uiBlocks = [];
  let i = 0;
  blocks.forEach(function(block) {
    let left = block.rect.left * scaleX;
    let right = block.rect.right * scaleX;
    let top = block.rect.top * scaleY;
    let bottom = block.rect.bottom * scaleY;

    let uiBlock = null;
    if (block.type === 'Text') {
      let red = Math.ceil(((100 - block.quality) * 255) / 100);
      let green = Math.ceil((block.quality * 255) / 100);
      let blue = 0;
      let color = 'rgb(' + [red, green, blue].join(',') + ')';
      uiBlock = (
        <Rect
          x={left}
          y={top}
          width={right - left}
          height={bottom - top}
          stroke={color}
          fill={color}
          opacity={0.4}
          key={i}
        />
      );
    } else {
      uiBlock = (
        <Rect
          x={left}
          y={top}
          width={right - left}
          height={bottom - top}
          stroke="rgb(200,200,200)"
          opacity={0.2}
          key={i}
        />
      );
    }

    uiBlocks.push(uiBlock);
    i = i + 1;
  });
  return <G>{uiBlocks}</G>;
};

export default props => {
  const [parentLayout, setParentLayout] = useState({});
  const [image, setImage] = useState({});
  const [isSettingsVisible, setSettingsVisible] = useState(false);
  const [rawCallData, setRawCallData] = useState({
    request: 'Select image and apply operation to see raw result',
    result: '',
  });
  const [isRawCallDataVisible, setRawCallDataVisible] = useState(false);

  const isActionDisabled = image.imageUri === undefined;
  const actionButtonColor = isActionDisabled ? 'grey' : 'dodgerblue';
  let svg = null;
  if (image.imageUri) {
    let imageViewLayout = updateImageViewLayout(parentLayout, image);

    const scaleX = imageViewLayout.width / image.imageSize.width;
    const scaleY = imageViewLayout.height / image.imageSize.height;

    let assessmentBlocks = null;
    if (image.qualityAssessmentForOcrBlocks) {
      assessmentBlocks = assessQualityForOcrUIBlocks(
        scaleX,
        scaleY,
        image.qualityAssessmentForOcrBlocks,
      );
    }

    let boundary = null;
    if (image.documentBoundary) {
      boundary = boundaryUI(scaleX, scaleY, image.documentBoundary);
    }

    let textLines = null;
    if (image.lines) {
      textLines = recognizedTextLinesUI(scaleX, scaleY, image.lines);
    }

    svg = (
      <Svg
        style={styles.image}
        height={imageViewLayout.height}
        width={imageViewLayout.width}>
        <SvgImage
          x={0}
          y={0}
          width={imageViewLayout.width}
          height={imageViewLayout.height}
          preserveAspectRatio="xMinYMin meet"
          href={image.imageUri}
        />
        {assessmentBlocks}
        {boundary}
        {textLines}
      </Svg>
    );
  }

  let settingsView = null;
  if (isSettingsVisible) {
    settingsView = (
      <SettingsView
        onClose={() => {
          setSettingsVisible(false);
        }}
      />
    );
  }
  const showSettings = () => {
    setSettingsVisible(true);
  };

  let rawResultView = null;
  if (isRawCallDataVisible) {
    let request = truncateBase64String(rawCallData.request);
    let result = truncateBase64String(rawCallData.result);
    const requestResultText =
      'Request:\n' +
      JSON.stringify(request, null, 2) +
      '\nResult:\n' +
      JSON.stringify(result, null, 2);
    rawResultView = (
      <Modal
        transparent={false}
        animationType="slide"
        onRequestClose={() => {
          setRawCallDataVisible(false);
        }}>
        <ScrollView>
          <Text style={styles.rawResultText} selectable={true}>
            {requestResultText}
          </Text>
        </ScrollView>
        <TouchableOpacity
          style={styles.closeRawResultButton}
          onPress={() => {
            setRawCallDataVisible(false);
          }}>
          <Text style={styles.mainButtonText}>CLOSE</Text>
        </TouchableOpacity>
      </Modal>
    );
  }

  const showRawResult = () => {
    setRawCallDataVisible(true);
  };

  return (
    <View
      style={styles.container}
      onLayout={event => {
        setParentLayout(event.nativeEvent.layout);
      }}>
      {rawResultView}
      {settingsView}
      <ScrollView style={styles.scrollView}>
        {svg}
        <View style={styles.resultView}>
          <Text>{image.text}</Text>
        </View>
      </ScrollView>
      <View style={styles.secondaryButtonRow}>
        <TouchableOpacity
          style={styles.rawResultButton}
          onPress={showRawResult}>
          <Text style={styles.secondaryButtonText}>RAW RESULT</Text>
        </TouchableOpacity>
        <TouchableOpacity style={styles.settingsButton} onPress={showSettings}>
          <Text style={styles.secondaryButtonText}>SETTINGS</Text>
        </TouchableOpacity>
      </View>
      <View style={styles.actionButtonRow}>
        <TouchableOpacity
          disabled={isActionDisabled}
          style={{...styles.button, backgroundColor: actionButtonColor}}
          onPress={() =>
            detectBoundary(image.imageUri, (request, result) => {
              setImage({...image, ...result});
              setRawCallData({request: request, result: result});
            })
          }>
          <Text style={styles.buttonText}>DETECT BOUNDARY</Text>
        </TouchableOpacity>
        <TouchableOpacity
          disabled={isActionDisabled}
          style={{...styles.button, backgroundColor: actionButtonColor}}
          onPress={() =>
            crop(image.imageUri, image.documentBoundary, (request, result) => {
              setImage(result);
              setRawCallData({request: request, result: result});
            })
          }>
          <Text style={styles.buttonText}>CROP</Text>
        </TouchableOpacity>
      </View>
      <View style={styles.actionButtonRow}>
        <TouchableOpacity
          disabled={isActionDisabled}
          style={{...styles.button, backgroundColor: actionButtonColor}}
          onPress={() =>
            rotate(image.imageUri, (request, result) => {
              setImage(result);
              setRawCallData({request: request, result: result});
            })
          }>
          <Text style={styles.buttonText}>ROTATE</Text>
        </TouchableOpacity>
        <TouchableOpacity
          disabled={isActionDisabled}
          style={{...styles.button, backgroundColor: actionButtonColor}}
          onPress={() =>
            assessQuality(image.imageUri, (request, result) => {
              setImage({...image, ...result});
              setRawCallData({request: request, result: result});
            })
          }>
          <Text style={styles.buttonText}>OCR QUALITY</Text>
        </TouchableOpacity>
      </View>
      <View style={styles.actionButtonRow}>
        <TouchableOpacity
          disabled={isActionDisabled}
          style={{...styles.button, backgroundColor: actionButtonColor}}
          onPress={() =>
            shareImage(image.imageUri, (request, result) => {
              setRawCallData({request: request, result: result});
            })
          }>
          <Text style={styles.buttonText}>SHARE IMAGE</Text>
        </TouchableOpacity>
        <TouchableOpacity
          disabled={isActionDisabled}
          style={{...styles.button, backgroundColor: actionButtonColor}}
          onPress={() =>
            shareToPdf(image.imageUri, (request, result) => {
              setRawCallData({request: request, result: result});
            })
          }>
          <Text style={styles.buttonText}>SHARE PDF</Text>
        </TouchableOpacity>
      </View>
      <View style={styles.actionButtonRow}>
        <TouchableOpacity
          disabled={isActionDisabled}
          style={{...styles.button, backgroundColor: actionButtonColor}}
          onPress={() =>
            recognize(image.imageUri, image.imageSize, async result => {
              if (result.imageUri === image.imageUri) {
                setImage({...image, ...result});
              } else {
                setImage(result);
              }
              setRawCallData({
                request: result.rawRequest,
                result: result.rawResult,
              });
            })
          }>
          <Text style={styles.buttonText}>RECOGNIZE TEXT</Text>
        </TouchableOpacity>
        <TouchableOpacity
          disabled={isActionDisabled}
          style={{...styles.button, backgroundColor: actionButtonColor}}
          onPress={() =>
            extract(image.imageUri, image.imageSize, async result => {
              if (result.imageUri === image.imageUri) {
                setImage({...image, ...result});
              } else {
                setImage(result);
              }
              setRawCallData({
                request: result.rawRequest,
                result: result.rawResult,
              });
            })
          }>
          <Text style={styles.buttonText}>BUSINESS CARD</Text>
        </TouchableOpacity>
      </View>
      <View style={styles.selectButtonRow}>
        <TouchableOpacity
          style={styles.button}
          onPress={() => {
            selectImage(result => {
              setImage(result);
              setRawCallData({
                result: result.rawResult,
                request: result.rawRequest,
              });
            });
          }}>
          <Text style={styles.mainButtonText}>SELECT IMAGE</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'stretch',
    justifyContent: 'center',
  },
  scrollView: {
    flex: 1,
  },
  resultView: {
    flexGrow: 1,
  },
  resultText: {
    fontSize: 20,
  },
  button: {
    flex: 1,
    backgroundColor: 'dodgerblue',
    height: 32,
    borderRadius: 5,
    justifyContent: 'center',
    alignItems: 'center',
    marginHorizontal: 8,
  },
  closeRawResultButton: {
    backgroundColor: 'dodgerblue',
    height: 48,
    borderRadius: 5,
    justifyContent: 'center',
    alignItems: 'center',
    marginHorizontal: 16,
    marginVertical: 16,
  },
  buttonText: {
    fontSize: 12,
    color: 'white',
  },
  secondaryButtonText: {
    fontSize: 12,
    color: 'gray',
    textAlign: 'right',
  },
  settingsButton: {
    height: 32,
    width: 100,
    borderRadius: 5,
    justifyContent: 'center',
    alignItems: 'flex-end',
    marginHorizontal: 8,
  },
  rawResultButton: {
    height: 32,
    width: 100,
    borderRadius: 5,
    justifyContent: 'center',
    alignItems: 'flex-start',
    marginHorizontal: 8,
  },
  secondaryButtonRow: {
    marginVertical: 8,
    marginHorizontal: 8,
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  actionButtonRow: {
    marginVertical: 8,
    marginHorizontal: 8,
    flexDirection: 'row',
    justifyContent: 'space-around',
    alignItems: 'center',
  },
  title: {
    flex: 1,
    fontSize: 20,
  },
  rawResultText: {
    flex: 1,
    fontSize: 16,
    marginVertical: 16,
    marginHorizontal: 16,
  },
  touchable: {
    fontSize: 20,
    padding: 10,
    color: 'dodgerblue',
  },
  image: {
    flex: 1,
    alignSelf: 'center',
  },
  mainButtonText: {
    fontSize: 20,
    color: 'white',
  },
  selectButtonRow: {
    height: 60,
    marginVertical: 8,
    paddingHorizontal: 8,
    paddingBottom: 16,
  },
});
