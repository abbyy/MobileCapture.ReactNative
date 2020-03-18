/// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
/// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

var settingsData = {
  licenseFileName: 'MobileCapture.License', // optional, default=MobileCapture.License

  cameraResolution: 'FullHD', // optional, default=FullHD (HD, FullHD, UHD_4K)
  cameraResolutionVariants: ['HD', 'FullHD', 'UHD_4K'],

  isFlashlightButtonVisible: true, // optional, default=true
  isCaptureButtonVisible: true, // optional, default=true
  isGalleryButtonVisible: true, // optional, default=true

  orientation: 'Default', // optional, default=default
  orientationVariants: ['Default', 'Portrait', 'Landscape'],

  isShowPreviewEnabled: false, // optional, default=false
  requiredPageCount: 0, // optional, default=0

  destination: 'File', // optional, captured image will be saved to corresponding file ("file") or returned as encode base64 image string ("base64"). default=file
  destinationVariants: ['File', 'Base64'],

  exportType: 'Jpg', // optional, default=jpg (jpg, png, pdf).
  exportVariants: ['Jpg', 'Png', 'Pdf'],

  compressionLevel: 'Low', // optional, default=Low (Low, Normal, High, ExtraHigh)
  compressionLevelVariants: ['Low', 'Normal', 'High', 'ExtraHigh'],

  defaultImageSettings: {
    minimumDocumentToViewRatio: 0.15, // optional, minimum document area relative to frame area - 0...1. Default 0.15.
    documentSize: 'Any', // optional, document size in millimeters e.g. '100x100'. default=Any. Constants: Any, A4, BusinessCard, Letter.
    documentSizeValues: ['Any', 'A4', 'BusinessCard', 'Letter', '100x100'],
    aspectRatioMin: 0.0, // optional, minimum aspect ratio of detected rectangle to capture. default 0 means any.
    aspectRatioMax: 0.0, // optional, maximum aspect ratio of detected rectangle to capture. default 0 means any.
    imageFromGalleryMaxSize: 4096, // optional, maximum width or height of image from gallery.
  },
};

export default () => settingsData;

export function updateSettings(settings) {
  settingsData = {...settingsData, ...settings};
}

export function updateImageSettings(imageSettings) {
  var newSetings = {...settingsData.defaultImageSettings, ...imageSettings};
  settingsData = {...settingsData, ...{defaultImageSettings: newSetings}};
}
