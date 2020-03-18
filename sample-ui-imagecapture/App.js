/// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
/// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

import React, {Component} from 'react';
import {View, SafeAreaView, StatusBar, Image, StyleSheet, Text, TouchableOpacity} from 'react-native';
import {KeyboardAwareScrollView} from 'react-native-keyboard-aware-scroll-view';
import SettingsView from './SettingsView';
import getSettings from './Settings';
import {startImageCapture} from 'react-native-abbyy-mobile-capture';
import ImageCaptureResultView from './ImageCaptureResultView';

export default class MobileCaptureSample extends Component {

  state = {imageCaptureResult: null};

  async _onPressCapture() {
    try {
      let result = await startImageCapture(getSettings());
      this.setState({imageCaptureResult: result});
    } catch (e) {
      console.log(e);
      this.setState({imageCaptureResult: e.message});
    }

    // Scroll to end after render.
      const scrollView = this._scrollView;
      setTimeout(() => {
        if(scrollView) {
          scrollView.scrollToEnd();
        }
      }, 0);
  }

  render() {
    return (
      <SafeAreaView style={styles.container}>
        <StatusBar barStyle="dark-content" />
        <KeyboardAwareScrollView 
          style={styles.container}
          ref={ref => this._scrollView = ref}
          keyboardDismissMode="on-drag">
            <Image style={styles.topLogo} source={require('./logo.png')} onError={({nativeEvent: {error}}) => console.log('Failed to render logo: ', error)} />
            <SettingsView />
            <View style={styles.bottomSpace}>
            <TouchableOpacity
              style={styles.captureButton}
              onPress={this._onPressCapture.bind(this)}>
              <Text style={styles.captureText}>Capture Images</Text>
            </TouchableOpacity>
          </View>
          <ImageCaptureResultView result={this.state.imageCaptureResult} />
        </KeyboardAwareScrollView>
      </SafeAreaView>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  topLogo: {
    resizeMode: 'contain',
    height: 100,
    width: '100%',
  },
  bottomSpace: {
    flexDirection: 'row',
  },
  captureButton: {
    backgroundColor: '#c60c30',
    alignItems: 'center',
    padding: 20,
    margin: 20,
    borderRadius: 10,
    flex: 1,
  },
  captureText: {
    fontSize: 20,
    color: 'white',
  },
});