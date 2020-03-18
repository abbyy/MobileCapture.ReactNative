/// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
/// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

import React, { useState } from 'react';
import {
  Modal,
  StyleSheet,
  Button,
  SafeAreaView,
  View,
} from 'react-native';

import PDFView from 'react-native-view-pdf';

/*
  props:
    filePath: String
    showPdf: Bool
    closeHandler: () => {}
*/
export default props => {
  const onClose = () => props.closeHandler();
  const [pdfView, setPdfView] = useState(null);
  const showPdf = props.showPdf;
  let filePath = props.filePath;

  if(showPdf) {
    console.log('Show pdf at path: ', filePath);
    if(Platform.OS === 'ios'){
      filePath = filePath.split('/').pop();
      console.log('ios extract filename: ', filePath);
    }
  }

  const reloadPdf = async () => {
    if(!pdfView) {
      return;
    }
    try {
      await pdfView.reload();
    } catch(error) {
      console.error(error);
    }
  }

  pdfViewComponent = (
    <PDFView
     style={styles.content}
     resource={filePath}
     resourceType={'file'}
     fileFrom={'tempDirectory'}
     onError={(error) => console.log('Failed render PDF', error)}
     ref={ref => setPdfView(ref)}
    />
  );

  if(showPdf) {
    reloadPdf();
  }

  return (
    <Modal onRequestClose={onClose} visible={showPdf}>
      <SafeAreaView style={styles.modalContainer}>
        {pdfViewComponent}  
        <Button title={'Close'} onPress={onClose} />
        <View style={{height: 20}} />
      </SafeAreaView>
    </Modal>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  modalContainer: {
    flex: 1,
    justifyContent: 'center',
  },
  content: {
    flex: 1,
    resizeMode: 'contain', // cover, contain, stretch, repeat, center
  },
});
