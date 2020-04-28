/// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
/// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.
import {NativeModules} from 'react-native';

const AbbyyMobileCapture = NativeModules.AbbyyMobileCapture;

// Opens a screen for the image capture scenario
//
// Default settings:
// {
//  licenseFileName: 'MobileCapture.License',
//  cameraResolution: 'FullHD',
//  isFlashlightButtonVisible: true,
//  isCaptureButtonVisible: true,
//  isGalleryButtonVisible: true,
//  orientation: 'Default',
//  isShowPreviewEnabled: false,
//  requiredPageCount: 0,
//  destination: 'File',
//  exportType: 'Jpg',
//  compressionLevel: 'Low',
//  defaultImageSettings: {
//    aspectRatioMin: 0.0,
//    aspectRatioMax: 0.0,
//    imageFromGalleryMaxSize: 4096,
//    minimumDocumentToViewRatio: 0.15,
//    documentSize: 'Any'
//  }
// }
//
// Result example:
// {
//  "images": [
//    {
//      "resultInfo": {
//        size: { width: 658, height: 1187 },
//        "exportType": "Jpg"
//      },
//      "filePath": "/data/user/0/com.abbyy.rtr.reactnative.sample/files/pages/page_334fd281-f472-4756-a4a0-d1f8d1857a0c.jpg"
//    }
//  ],
//  "resultInfo": {
//    uriPrefix: "file://"
//  }
// }
// 
// See full documentation at https://help.abbyy.com/en-us/mobilecapturesdk/1/reactnative_help/reactnative-abbyymobilecapture
export async function startImageCapture(settings) {
  return await AbbyyMobileCapture.startImageCapture(settings);
}

// Starts a text capture scenario for a single image
//
// Settings example:
// {
//   licenseFileName: 'MobileCapture.License',
//   imageUri: 'file:///data/user/0/com.abbyy.rtr.reactnative.sample/files/pages/page_334fd281-f472-4756-a4a0-d1f8d1857a0c.jpg',
//   isTextOrientationDetectionEnabled: true,
//   recognitionLanguages: ['English']
// }
//
// Result example:
// {
//   orientation: 90,
//   warnings: ['ProbablyLowQualityImage'],
//   text: "Test\ntext",
//   textBlocks: [
//     {
//       textLines: [
//         {
//           text: "Test",
//           quadrangle: [{x: 100, y: 200}, {x: 100, y: 50}, {x: 300, y: 50}, {x: 300, y: 200}],
//           rect: {left: 100, top: 50, right: 300, bottom: 200},
//           charInfo: [
//             { // For 'T'
//               quadrangle: ...,
//               rect: ...,
//               isItalic: true,
//               isBold: true,
//               isUnderlined: true,
//               isStrikethrough: true,
//               isSmallcaps: true,
//               isSuperscript: true,
//               isUncertain: true
//             },
//             { // For 'e' },
//             { // For 's' },
//             { // For 't' },
//           ]
//         },
//         ...
//       ]
//     },
//     ...
//   ]
// }
// 
// See full documentation at https://help.abbyy.com/en-us/mobilecapturesdk/1/reactnative_help/reactnative-abbyymobilecapture
export async function recognizeText(settings) {
  return await AbbyyMobileCapture.recognizeText(settings);
}

// Starts a data capture scenario for a single image
// 
// Settings example:
// {
//   licenseFileName: 'MobileCapture.License',
//   imageUri: 'data:image/jpeg;base64,...', // Works with base64 as well as with files.
//   profile: 'BusinessCards',
//   isTextOrientationDetectionEnabled: true,
//   recognitionLanguages: ['English']
// }
//
// Result example:
// {
//   orientation: 0,
//   warnings: ['ProbablyWrongLanguage'],
//   dataFields: [
//     {
//       id: "FirstName",
//       name: "First Name",
//       text: "John",
//       quadrangle: [{ x: 100, y: 100 }, ...],
//       rect: { left: 100, ... },
//       charInfo: [
//         { // For 'J'
//           quadrangle: ...,
//           rect: ...,
//           isUncertain: false
//         },
//         ...
//       ],
//       components: [
//         { id: ..., name: ..., ... }
//       ]
//     },
//     ...
//   ]
// }
// 
// See full documentation at https://help.abbyy.com/en-us/mobilecapturesdk/1/reactnative_help/reactnative-abbyymobilecapture
export async function extractData(settings) {
  return await AbbyyMobileCapture.extractData(settings);
}

// Detects a quadrangle representing document boundary on an image
//
// Settings example:
// {
//   licenseFileName: 'MobileCapture.License',
//   imageUri: 'file:///data/user/0/...',
// }
// 
// Result example:
// {
//   documentBoundary: [
//     {x: 50, y: 600},
//     {x: 50, y: 100},
//     {x: 340, y: 120},
//     {x: 330, y: 590},
//   ]
// }
// 
// See full documentation at https://help.abbyy.com/en-us/mobilecapturesdk/1/reactnative_help/reactnative-abbyymobilecapture
export async function detectDocumentBoundary(settings) {
  return await AbbyyMobileCapture.detectDocumentBoundary(settings);
}

// Crops image according to the document boundary and size
//
// Settings example:
// {
//   licenseFileName: 'MobileCapture.License',
//   imageUri: 'file:///data/user/0/...',
//   documentBoundary: [
//     {x: 50, y: 600},
//     {x: 50, y: 100},
//     {x: 340, y: 120},
//     {x: 330, y: 590},
//   ],
//   result: {
//     destination: 'Base64',
//     compressionLevel: 'Low',
//     exportType: 'Jpg'
//   }
// }
//
// Result example:
// {
//   imageUri: 'data:image/jpeg;base64,...',
// }
// 
// See full documentation at https://help.abbyy.com/en-us/mobilecapturesdk/1/reactnative_help/reactnative-abbyymobilecapture
export async function cropImage(settings) {
  return await AbbyyMobileCapture.cropImage(settings);
}

// Rotates image by specified angle
//
// Settings example:
// {
//   licenseFileName: 'MobileCapture.License',
//   imageUri: 'data:image/png;base64,...',
//   angle: 180,
//   result: {
//     destination: 'File',
//   }
// }
//
// Result example:
// {
//   imageUri: 'file:///data/user/0/...',
// }
// 
// See full documentation at https://help.abbyy.com/en-us/mobilecapturesdk/1/reactnative_help/reactnative-abbyymobilecapture
export async function rotateImage(settings) {
  return await AbbyyMobileCapture.rotateImage(settings);
}

// Estimates if an image quality is suitable for OCR
//
// Settings example:
// {
//   licenseFileName: 'MobileCapture.License',
//   imageUri: 'file:///data/user/0/...',
// }
//
// Result example:
// {
//   qualityAssessmentForOcrBlocks: [
//     {
//       type: 'Text',
//       quality: 90,
//       rect: {
//         top: 100,
//         bottom: 200,
//         left: 100,
//         right: 200
//       }
//     },
//     {
//       type: 'Unknown',
//       quality: 70,
//       rect: {
//         top: 100,
//         bottom: 200,
//         left: 200,
//         right: 300
//       }
//     },
//     ...
//   ]
// }
// 
// See full documentation at https://help.abbyy.com/en-us/mobilecapturesdk/1/reactnative_help/reactnative-abbyymobilecapture
export async function assessQualityForOcr(settings) {
  return await AbbyyMobileCapture.assessQualityForOcr(settings);
}

// Exports an image to JPG or PNG format
// 
// Settings example:
// {
//   licenseFileName: 'MobileCapture.License',
//   imageUri: 'file:///data/user/0/...',
//   result: {
//     destination: 'Base64',
//     compressionLevel: 'Low',
//     exportType: 'Png'
//   }
// }
//
// Result example:
// {
//   imageUri: 'data:image/png;base64,...',
// }
// 
// See full documentation at https://help.abbyy.com/en-us/mobilecapturesdk/1/reactnative_help/reactnative-abbyymobilecapture
export async function exportImage(settings) {
  return await AbbyyMobileCapture.exportImage(settings);
}

// Exports images to PDF format
// 
// Settings example:
// {
//   licenseFileName: 'MobileCapture.License',
//   images: [
//     {
//       imageUri: 'file:///data/user/0/...',
//       compressionLevel: 'Low',
//       pageSize: {width: 100, height:200},
//     },
//     {
//       imageUri: 'file:///data/user/0/...',
//       compressionLevel: 'Normal',
//       pageSize: {width: 200, height:300},
//     }
//   ],
//   result: {
//     destination: 'File',
//     filePath: "/data/user/0/.../dir1/file.pdf",
//     pdfInfo: {
//       title: 'Title',
//       author: 'Author'
//     }
//   }
// }
//
// Result example:
// {
//   pdfUri: 'file:///data/user/0/.../dir1/file.pdf',
// }
// 
// See full documentation at https://help.abbyy.com/en-us/mobilecapturesdk/1/reactnative_help/reactnative-abbyymobilecapture
export async function exportImagesToPdf(settings) {
  return await AbbyyMobileCapture.exportImagesToPdf(settings);
}
