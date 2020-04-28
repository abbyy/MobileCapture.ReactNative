/// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
/// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

import React from 'react';

import {SafeAreaView, StatusBar, StyleSheet} from 'react-native';

import CoreAPIView from './CoreAPIView';

export default () => {
  return (
    <SafeAreaView style={styles.container}>
      <StatusBar barStyle="dark-content" />
      <CoreAPIView style={styles.content} />
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    justifyContent: 'flex-end',
    flex: 1,
  },
  content: {
    backgroundColor: 'lightgray',
    flex: 1,
  },
});
