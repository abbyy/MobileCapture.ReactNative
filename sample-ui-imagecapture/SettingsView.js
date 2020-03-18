/// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
/// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

import React, {useState} from 'react';
import {
  StyleSheet,
  View,
  Text,
  Switch,
  TouchableOpacity,
  TextInput,
} from 'react-native';
import getSettings, {updateSettings, updateImageSettings} from './Settings';
import ActionSheet from './ActionSheet';

export default () => {
  const settings = getSettings();

  const [orientation, setOrientation] = useState(settings.orientation);
  // Update methods need because of auto freeze settings in native code.
  const updateOrientation = value => {
    setOrientation(value);
    updateSettings({orientation: value});
  };

  const [isFlashButtonVisible, setFlashButtonVisible] = useState(settings.isFlashlightButtonVisible);
  const updateFlashVisible = value => {
    setFlashButtonVisible(value);
    updateSettings({isFlashlightButtonVisible: value});
  };

  const [isCaptureButtonVisible, setCaptureButtonVisible] = useState(settings.isCaptureButtonVisible);
  const updateCaptureButtonVisible = value => {
    setCaptureButtonVisible(value);
    updateSettings({isCaptureButtonVisible: value});
  };

  const [isGalleryButtonVisible, setGalleryButtonVisible] = useState(settings.isGalleryButtonVisible);
  const updateGalleryButtonVisible = value => {
    setGalleryButtonVisible(value);
    updateSettings({isGalleryButtonVisible: value});
  };

  const [showPreview, setShowPreview] = useState(settings.isShowPreviewEnabled);
  const updateShowPreview = value => {
    setShowPreview(value);
    updateSettings({isShowPreviewEnabled: value});
  };

  const [requiredPageCount, setRequiredPageCount] = useState(settings.requiredPageCount);
  const updateRequiredPageCount = value => {
    setRequiredPageCount(Number(value));
    updateSettings({requiredPageCount: Number(value)});
  };

  const [resolution, setResolution] = useState(settings.cameraResolution);
  const updateResolution = value => {
    setResolution(value);
    updateSettings({cameraResolution: value});
  };

  const [destination, setDestination] = useState(settings.destination);
  const updateDestination = value => {
    setDestination(value);
    updateSettings({destination: value});
  };

  const [exportType, setExportType] = useState(settings.exportType);
  const updateExportType = value => {
    setExportType(value);
    updateSettings({exportType: value});
  };

  const [compressionLevel, setCompLevel] = useState(settings.compressionLevel);
  const updateCompressionLevel = value => {
    setCompLevel(value);
    updateSettings({compressionLevel: value});
  };

  const [documentSize, setDocumentSize] = useState(
    settings.defaultImageSettings.documentSize,
  );
  const updateDocumentSize = value => {
    setDocumentSize(value);
    updateImageSettings({documentSize: value});
  };

  parseLocaleFloat = value => { return parseFloat(value.replace(',', '.')); }
  
  const [minDocToViewRatio, setMinDocToViewRatio] = useState(
    String(settings.defaultImageSettings.minimumDocumentToViewRatio),
  );
  const updateMinDocToViewRatio = value => {
    setMinDocToViewRatio(value);

    const result = parseLocaleFloat(value);
    if (!isNaN(result)) {
      updateImageSettings({minimumDocumentToViewRatio: result});
    }
  };

  const [aspectRatioMin, setAspectRatioMin] = useState(
    String(settings.defaultImageSettings.aspectRatioMin),
  );
  const updateAspectRatioMin = value => {
    setAspectRatioMin(value);

    const result = parseLocaleFloat(value);
    if (!isNaN(result)) {
      updateImageSettings({aspectRatioMin: result});
    }
  };

  const [aspectRatioMax, setAspectRatioMax] = useState(
    String(settings.defaultImageSettings.aspectRatioMax),
  );
  const updateAspectRatioMax = value => {
    setAspectRatioMax(value);

    const result = parseLocaleFloat(value);
    if (!isNaN(result)) {
      updateImageSettings({aspectRatioMax: result});
    }
  };

  const [imageFromGalleryMaxSize, setImageFromGalleryMaxSize] = useState(
    settings.defaultImageSettings.imageFromGalleryMaxSize,
  );
  const updateImageFromGalleryMaxSize = value => {
    setImageFromGalleryMaxSize(Number(value));
    updateImageSettings({imageFromGalleryMaxSize: Number(value)});
  };

  return (
    <View style={styles.container}>
        <View style={styles.row}>
          <Text style={styles.title}>Orientation</Text>
          <TouchableOpacity
            onPress={() =>
              ActionSheet(settings.orientationVariants, updateOrientation)
            }>
            <Text style={styles.touchable}>{orientation}</Text>
          </TouchableOpacity>
        </View>
        <View style={styles.row}>
          <Text style={styles.title}>Flash button visible</Text>
          <Switch
            value={isFlashButtonVisible}
            onValueChange={updateFlashVisible}
          />
        </View>
        <View style={styles.row}>
          <Text style={styles.title}>Capture button visible</Text>
          <Switch
            value={isCaptureButtonVisible}
            onValueChange={updateCaptureButtonVisible}
          />
        </View>
        <View style={styles.row}>
          <Text style={styles.title}>Gallery button visible</Text>
          <Switch
            value={isGalleryButtonVisible}
            onValueChange={updateGalleryButtonVisible}
          />
        </View>
        <View style={styles.row}>
          <Text style={styles.title}>Show preview enabled</Text>
          <Switch value={showPreview} onValueChange={updateShowPreview} />
        </View>
        <View style={styles.row}>
          <Text style={styles.title}>Required page count</Text>
          <TextInput
            style={styles.textInput}
            value={String(requiredPageCount)}
            keyboardType="numeric"
            onChangeText={updateRequiredPageCount}
          />
        </View>
        <View style={styles.row}>
          <Text style={styles.title}>Camera resolution</Text>
          <TouchableOpacity
            onPress={() =>
              ActionSheet(settings.cameraResolutionVariants, updateResolution)
            }>
            <Text style={styles.touchable}>{resolution}</Text>
          </TouchableOpacity>
        </View>
        <View style={styles.row}>
          <Text style={styles.title}>Destination</Text>
          <TouchableOpacity
            onPress={() =>
              ActionSheet(settings.destinationVariants, updateDestination)
            }>
            <Text style={styles.touchable}>{destination}</Text>
          </TouchableOpacity>
        </View>
        <View style={styles.row}>
          <Text style={styles.title}>Export type</Text>
          <TouchableOpacity
            onPress={() => ActionSheet(settings.exportVariants, updateExportType)}>
            <Text style={styles.touchable}>{exportType}</Text>
          </TouchableOpacity>
        </View>
        <View style={styles.row}>
          <Text style={styles.title}>Compression level</Text>
          <TouchableOpacity
            onPress={() =>
              ActionSheet(settings.compressionLevelVariants, updateCompressionLevel)
            }>
            <Text style={styles.touchable}>{compressionLevel}</Text>
          </TouchableOpacity>
        </View>
        <Text style={headerStyle}>Default Image Settings</Text>
        <View style={styles.row}>
          <Text style={styles.title}>Document size</Text>
          <TouchableOpacity
            onPress={() =>
              ActionSheet(
                settings.defaultImageSettings.documentSizeValues,
                updateDocumentSize,
              )
            }>
            <Text style={styles.touchable}>{documentSize}</Text>
          </TouchableOpacity>
        </View>
        <View style={styles.row}>
          <Text style={styles.title}>Min document to view ratio</Text>
          <TextInput
            style={styles.textInput}
            value={String(minDocToViewRatio)}
            keyboardType="numeric"
            onChangeText={updateMinDocToViewRatio}
          />
        </View>
        <View style={styles.row}>
          <Text style={styles.title}>Aspect ratio min</Text>
          <TextInput
            style={styles.textInput}
            value={String(aspectRatioMin)}
            keyboardType="numeric"
            onChangeText={updateAspectRatioMin}
          />
        </View>
        <View style={styles.row}>
          <Text style={styles.title}>Aspect ratio max</Text>
          <TextInput
            style={styles.textInput}
            value={String(aspectRatioMax)}
            keyboardType="numeric"
            onChangeText={updateAspectRatioMax}
          />
        </View>
        <View style={styles.row}>
          <Text style={styles.title}>Gallery image max size</Text>
          <TextInput
            style={styles.textInput}
            value={String(imageFromGalleryMaxSize)}
            keyboardType="numeric"
            onChangeText={updateImageFromGalleryMaxSize}
          />
        </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  row: {
    flex: 1,
    flexDirection: 'row',
    alignItems: 'center',
    margin: 8,
    marginHorizontal: 16,
  },
  title: {
    flex: 1,
    fontSize: 20,
  },
  touchable: {
    fontSize: 20,
    padding: 4,
    color: 'dodgerblue',
  },
  textInput: {
    fontSize: 20,
    borderWidth: 2,
    color: 'black',
    borderColor: 'darkgray',
    borderRadius: 5,
    padding: 4,
    width: 80,
    textAlign: 'right',
  },
});

const headerStyle = [
  styles.row,
  styles.title,
  {marginTop: 20, color: 'darkgray'},
];
