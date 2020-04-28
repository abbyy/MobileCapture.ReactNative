import React, {useState} from 'react';
import {
  View,
  FlatList,
  TouchableOpacity,
  Text,
  StyleSheet,
  SafeAreaView,
} from 'react-native';

// props: {title: String, variants: array, values: array, variantLabels: object completion: (values)=>{}}
export default props => {
  const values = new Set(props.values);

  const Item = params => {
    const key = params.item;
    const [selected, setSelected] = useState(props.values.includes(key));
    return (
      <>
        <TouchableOpacity
          style={styles.row}
          onPress={() => {
            if (selected) {
              values.delete(key);
            } else {
              values.add(key);
            }
            setSelected(!selected);
          }}>
          <Text style={styles.rowText}>{props.variantLabels[key]}</Text>
          <Text style={styles.checkMark}>
            {values.has(key) ? '\u2713' : ''}
          </Text>
        </TouchableOpacity>
        <View style={styles.separator} />
      </>
    );
  };

  return (
    <SafeAreaView>
      <View style={styles.topBar}>
        <Text style={styles.titleText}>{props.title}</Text>
        <TouchableOpacity
          style={styles.doneButton}
          onPress={() => {
            props.completion([...values]);
          }}>
          <Text style={styles.doneText}>Done</Text>
        </TouchableOpacity>
      </View>
      <FlatList
        data={props.variants}
        renderItem={({item}) => <Item item={item} />}
        keyExtractor={item => item}
      />
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  topBar: {
    justifyContent: 'flex-end',
    flexDirection: 'row',
    backgroundColor: 'dodgerblue',
  },
  titleText: {
    position: 'absolute',
    width: '100%',
    color: 'white',
    textAlign: 'center',
    alignSelf: 'center',
    fontWeight: 'bold',
    fontSize: 20,
  },
  doneButton: {
    margin: 20,
  },
  doneText: {
    fontSize: 20,
    color: 'white',
  },
  row: {
    alignItems: 'center',
    flexDirection: 'row',
    padding: 10,
  },
  rowText: {
    fontSize: 20,
    flexGrow: 1,
  },
  checkMark: {
    color: 'dodgerblue',
    fontSize: 20,
  },
  separator: {
    marginLeft: 16,
    height: 1,
    backgroundColor: 'lightgray',
  },
});
