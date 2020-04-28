/// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
/// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

import ActionSheet from 'rn-actionsheet-module';

export default (title, variants, onChange) => {
  if (!variants.length) {
    console.error('variants must not be empty');
    return;
  }

  ActionSheet(
    {
      title: title,
      optionsIOS: [...variants, 'Cancel'],
      optionsAndroid: variants,
      cancelButtonIndex: variants.length,
      onCancelAndroidIndex: variants.length,
    },
    buttonIndex => {
      if (buttonIndex !== variants.length) {
        onChange(variants[buttonIndex]);
      }
    },
  );
};
