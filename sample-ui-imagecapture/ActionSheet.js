/// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
/// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

import ActionSheet from 'rn-actionsheet-module'

export default (variants, completion) => {
  if (!variants.length) {
    console.error('variants must not be empty');
    return;
  }

  ActionSheet(
    {
      optionsIOS: [...variants, 'Cancel'],
      optionsAndroid: variants,
      cancelButtonIndex: variants.length,
      onCancelAndroidIndex: variants.length,
    },
    buttonIndex => {
      if(buttonIndex != variants.length) {
        completion(variants[buttonIndex]);
      }
    }
  );
};
