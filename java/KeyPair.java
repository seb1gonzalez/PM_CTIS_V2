package mysql;

public class KeyPair<K, V> {
	K key;
	V value;
	enum ValClass{
		STRING, DOUBLE, INT;
	}
	ValClass type;
	KeyPair(K key, V val, ValClass type){
		this.key = key;
		value = val;
		this.type = type;
	}
}
