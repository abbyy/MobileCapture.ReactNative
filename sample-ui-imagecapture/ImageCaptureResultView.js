/// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
/// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

import React, { useState } from 'react'
import {StyleSheet, FlatList, View, Text, Image, Button} from 'react-native'
import PDFView from './PDFView'

export default props => {
  if(!props.result) {
    return null;
  }

  let resultView = (
    <View style={styles.resultInfo}>
      <Text style={styles.resultText}>{JSON.stringify(props.result, null, 2).substring(0, 1024)}</Text>
    </View>
  );
  const [showPdf, setShowPdf] = useState(false);
  let isPdfVisible = showPdf;
  
  let imageView = null;
  let pdfInfo = props.result.pdfInfo;
  let images = props.result.images;
  let resultInfo = props.result.resultInfo;
  if(pdfInfo) {
    let filePath = pdfInfo.filePath;
    if(Platform.OS === 'ios') {
      filePath = filePath.split('/').pop();
    }

    imageView = (
      <>
        <Button title="Show PDF" onPress={() => {
            isPdfVisible = true;
            setShowPdf(true);
          }} 
        />
        <View style={{height: 20}} />
        <PDFView
          filePath={filePath}
          showPdf={isPdfVisible}
          closeHandler={() => {
             isPdfVisible = false;
             setShowPdf(false);
          }}
        />
      </>
    );
  } else if(images) {
    imageView = (
      <FlatList 
      style={styles.container}
      contentContainerStyle={styles.content}
      horizontal={true} 
      data={props.result.images}
      keyExtractor={(item, index) => '' +index}
      renderItem={({item}) => {
        return (<Image style={styles.image} source={imageSource(resultInfo.uriPrefix, item)} onError={({nativeEvent: {error}}) => console.log('Failed to render image: ', error)} />);
      }} 
    />
    );
  }

  const imageSource = (uriPrefix, item) => {
    let uriValue = null;
    if(item.filePath) {
      uriValue = uriPrefix + item.filePath;
    } else {
      uriValue = uriPrefix + item.base64;
    }
    return {uri: uriValue}
  };

  return (
    <>
    {imageView}
    {resultView}
    </>
  );  
} 

const styles = StyleSheet.create({
  container: {
    height: 300,
    flexDirection: 'row',
    backgroundColor: 'gray',
  },
  content: {
    flexGrow: 1,
  },
  resultInfo: {
    backgroundColor: 'lightgray',
  },
  resultText: {
    fontFamily: Platform.OS === 'ios' ? 'Courier' : 'monospace',
  },
  image: {
    resizeMode: 'contain',
    height: 300,
    width: 200,
    padding: 10,
  }
});