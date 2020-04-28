/// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
/// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

import React, {useState} from 'react';
import {
  Modal,
  StyleSheet,
  SafeAreaView,
  View,
  Text,
  TouchableOpacity,
  Switch,
  TextInput,
} from 'react-native';
import ActionSheet from './ActionSheet';
import MultipleChoiseSheet from './MultipleChoiseSheet';

const detectionModeVariants = ['Default', 'Fast'];
const compressionLevelVariants = ['Low', 'Normal', 'High', 'ExtraHigh'];
const destinationVariants = ['File', 'Base64'];
const exportTypeVariants = ['Jpg', 'Png'];
const angleVariants = ['90', '180', '270'];

const languageToCode = {
  English: 'EN',
  Russian: 'RU',
  German: 'DE',
  French: 'FR',
  Italian: 'IT',
  Spanish: 'ES',
};

const languageVariants = [];
for (const key of Object.keys(languageToCode)) {
  languageVariants.push(key);
}

const languageVariantLabels = {};
for (const key of languageVariants) {
  languageVariantLabels[key] = key;
}

const languagesToString = languages => {
  return languages.map(language => languageToCode[language]).join(', ');
};

const MultipleChoiceRow = props => {
  const title = props.title;
  const variants = props.variants;
  const variantLabels = props.variantLabels;
  const onChange = props.onChange;
  const variantsToString = props.variantsToString;

  const [isVisible, setVisible] = useState(false);
  const [values, setValues] = useState(props.values);

  return (
    <View style={styles.row}>
      <Modal
        transparent={false}
        animationType="slide"
        visible={isVisible}
        onRequestClose={() => {
          setVisible(false);
        }}>
        <MultipleChoiseSheet
          title={title}
          values={values}
          variants={variants}
          variantLabels={variantLabels}
          completion={newValues => {
            setVisible(false);
            setValues(newValues);
            onChange(newValues);
          }}
        />
      </Modal>

      <Text style={styles.title}>{title}</Text>
      <TouchableOpacity
        onPress={() => {
          setVisible(true);
        }}>
        <Text style={styles.touchable}>{variantsToString(values)}</Text>
      </TouchableOpacity>
    </View>
  );
};

const SwitchRow = props => {
  const title = props.title;
  const [isEnabled, setEnabled] = useState(props.isEnabled);
  const onChange = props.onChange;

  const toggleSwitch = () => {
    const newValue = !isEnabled;
    setEnabled(newValue);
    onChange(newValue);
  };

  return (
    <View style={styles.row}>
      <Text style={styles.title}>{title}</Text>
      <Switch
        trackColor={{false: '#767577', true: '#81b0ff'}}
        thumbColor={isEnabled ? 'dodgerblue' : '#f4f3f4'}
        ios_backgroundColor="#3e3e3e"
        onValueChange={toggleSwitch}
        value={isEnabled}
      />
    </View>
  );
};

const SingleChoiceRow = props => {
  const title = props.title;
  const [value, setValue] = useState(props.value);
  const variants = props.variants;
  const onChange = props.onChange;

  return (
    <View style={styles.row}>
      <Text style={styles.title}>{title}</Text>
      <TouchableOpacity
        onPress={() => {
          ActionSheet(title, variants, newValue => {
            setValue(newValue);
            onChange(newValue);
          });
        }}>
        <Text style={styles.touchable}>{value}</Text>
      </TouchableOpacity>
    </View>
  );
};

const TextInputRow = props => {
  const title = props.title;
  const [value, setValue] = useState(props.value);
  const onChange = props.onChange;

  return (
    <View style={styles.row}>
      <Text style={styles.title}>{title}</Text>
      <TextInput
        style={styles.textInput}
        onChangeText={text => {
          setValue(text);
          onChange(text);
        }}
        value={value}
      />
    </View>
  );
};

export const settings = {
  isTextOrientationDetectionEnabled: true,
  languages: [languageVariants[0]],
  detectionMode: detectionModeVariants[0],
  compressionLevel: compressionLevelVariants[0],
  exportType: exportTypeVariants[0],
  destination: destinationVariants[0],
  angle: angleVariants[0],
  pdfInfoTitle: 'ReactNative PDF',
};

export default props => {
  const onClose = props.onClose;
  return (
    <Modal onRequestClose={onClose}>
      <SafeAreaView style={styles.modalContainer}>
        <SwitchRow
          title="Detect text orientation"
          isEnabled={settings.isTextOrientationDetectionEnabled}
          onChange={isEnabled =>
            (settings.isTextOrientationDetectionEnabled = isEnabled)
          }
        />
        <MultipleChoiceRow
          title="Languages"
          variants={languageVariants}
          variantLabels={languageVariantLabels}
          values={settings.languages}
          variantsToString={languagesToString}
          onChange={value => (settings.languages = value)}
        />
        <SingleChoiceRow
          title="Boundary detection mode"
          value={settings.detectionMode}
          variants={detectionModeVariants}
          onChange={value => (settings.detectionMode = value)}
        />
        <SingleChoiceRow
          title="Compression level"
          value={settings.compressionLevel}
          variants={compressionLevelVariants}
          onChange={value => (settings.compressionLevel = value)}
        />
        <SingleChoiceRow
          title="Export type"
          value={settings.exportType}
          variants={exportTypeVariants}
          onChange={value => (settings.exportType = value)}
        />
        <SingleChoiceRow
          title="Destination"
          value={settings.destination}
          variants={destinationVariants}
          onChange={value => (settings.destination = value)}
        />
        <SingleChoiceRow
          title="Rotation angle"
          value={settings.angle.toString()}
          variants={angleVariants}
          onChange={value => (settings.angle = Number(value))}
        />
        <TextInputRow
          title="PDF info title"
          value={settings.pdfInfoTitle}
          onChange={value => (settings.pdfInfoTitle = value)}
        />
        <View style={styles.emptyView} />
        <View style={styles.closeButtonRow}>
          <TouchableOpacity style={styles.button} onPress={onClose}>
            <Text style={styles.closeButtonText}>CLOSE</Text>
          </TouchableOpacity>
        </View>
      </SafeAreaView>
    </Modal>
  );
};

const styles = StyleSheet.create({
  modalContainer: {
    flex: 1,
    justifyContent: 'flex-end',
  },
  button: {
    flex: 1,
    backgroundColor: 'dodgerblue',
    height: 32,
    borderRadius: 5,
    justifyContent: 'center',
    alignItems: 'center',
    marginHorizontal: 8,
  },
  content: {
    flex: 1,
    resizeMode: 'contain',
  },
  row: {
    flexDirection: 'row',
    alignItems: 'center',
    marginHorizontal: 16,
  },
  title: {
    flex: 1,
    fontSize: 20,
  },
  touchable: {
    fontSize: 20,
    padding: 10,
    color: 'dodgerblue',
  },
  closeButtonText: {
    fontSize: 20,
    color: 'white',
  },
  closeButtonRow: {
    height: 60,
    marginVertical: 8,
    paddingHorizontal: 8,
    paddingBottom: 16,
  },
  textInput: {
    fontSize: 20,
    borderWidth: 2,
    color: 'black',
    borderColor: 'darkgray',
    borderRadius: 5,
    padding: 4,
    width: 200,
    textAlign: 'right',
  },
  emptyView: {
    flexGrow: 1,
  },
});
