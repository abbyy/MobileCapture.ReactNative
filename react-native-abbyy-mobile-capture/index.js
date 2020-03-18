/// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
/// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.
import {NativeModules} from 'react-native';

const AbbyyMobileCapture = NativeModules.AbbyyMobileCapture;

/// Open modal dialog for Image Capture Scenario.
export async function startImageCapture(settings) {
  const result = await AbbyyMobileCapture.startImageCapture(settings);
  return result;
}
