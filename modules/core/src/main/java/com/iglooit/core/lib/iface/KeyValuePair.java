package com.iglooit.core.lib.iface;

import java.util.*;

/**
 * This class implements a key-value pair.
 */
public class KeyValuePair<K, V>
    implements Comparable<KeyValuePair<K, V>>,
    Map.Entry<K, V>
{
    private K key = null;
    private V value = null;

    /**
     * Constructs a key value pair with unspecified key and value.
     */
    public KeyValuePair()
    {
    }

    /**
     * Constructs a key value pair with the specified key and value.
     * <p/>
     * The Comparable interface is implemented at the key level. If this object
     * is ever to be used in an environment where the Comparable interface
     * is a necessity, the key must implement Comparable.
     */
    public KeyValuePair(K key, V value)
    {
        this();
        this.key = key;
        this.value = value;
    }

    /**
     * Compares the specified KeyValuePair with this key value pair for equality. Returns true if the given
     * KeyValuePair is also a  key value pair and the two pairs represent the same mapping. More formally,
     * two pairs p1 and p2 represent the same mapping if
     * <pre>
     *        (p1.getKey()==null ? p2.getKey()==null : p1.getKey().equals(p2.getKey()))
     *     && (p1.getValue()==null ? p2.getValue()==null : p1.getValue().equals(p2.getValue()))
     * </pre>
     * This ensures that the equals method works properly across different implementations of the Map.Entry
     * interface.
     *
     * @param other KeyValuePair to be compared for equality with this key value pair.
     * @return true if the specified KeyValuePair is equal to this key value pair.
     */
    public synchronized boolean equals(KeyValuePair other)
    {
        synchronized (other)
        {
            return (key == null ? other.key == null : ((K)key).equals(other.key))
                && (value == null ? other.value == null : ((V)value).equals(other.value));
        }
    }

    /**
     * Compares the specified KeyValuePair with this key value pair for equality. Returns true if the given
     * KeyValuePair is also a  key value pair and the two pairs represent the same mapping. More formally,
     * two pairs p1 and p2 represent the same mapping if
     * <pre>
     *        (p1.getKey()==null ? p2.getKey()==null : p1.getKey().equals(p2.getKey()))
     *     && (p1.getValue()==null ? p2.getValue()==null : p1.getValue().equals(p2.getValue()))
     * </pre>
     * This ensures that the equals method works properly across different implementations of the Map.Entry
     * interface.
     *
     * @param other KeyValuePair to be compared for equality with this key value pair.
     * @return true if the specified KeyValuePair is equal to this key value pair.
     */
    public synchronized boolean equals(Object other)
    {
        if (other instanceof KeyValuePair)
            return equals((KeyValuePair)other);
        else
            return false;
    }

    /**
     * Returns the hash code value for this key value pair. The hash code of a key value pair e is defined to be:
     * <pre>
     *       (e.getKey()==null   ? 0 : e.getKey().hashCode())
     *     ^ (e.getValue()==null ? 0 : e.getValue().hashCode())
     * </pre>
     * This ensures that p1.equals(p2) implies that p1.hashCode()==p2.hashCode() for any two pairs
     * p1 and p2, as required by the general contract of Object.hashCode.
     *
     * @return the hash code value for this key value pair.
     */
    public synchronized int hashCode()
    {
        return (key == null ? 0 : key.hashCode())
            ^ (value == null ? 0 : value.hashCode());
    }

    /**
     * Returns the key component of this key value pair.
     */
    public synchronized K getKey()
    {
        return key;
    }

    /**
     * Returns the value component of this key value pair.
     */
    public synchronized V getValue()
    {
        return value;
    }

    /**
     * Returns the value component of the KeyValuePair object which belongs to the
     * supplied collection and has the specified key value.
     *
     * @param key    The key of the KeyValue pair object to find.
     * @param source The collection of KeyValuePair objects to be searched for the specified key.
     * @return The value of the KeyValuePair object having the specified key or null if the
     *         collection does not contain an object having the specified key.
     */
    public static Object getValue(Collection<KeyValuePair> source, Object key)
    {
        for (KeyValuePair keyValuePair : source)
        {
            if (key.equals(keyValuePair.getKey()))
                return keyValuePair.getValue();
        }

        return null;
    }

    /**
     * Returns an indicator as to whether or not a KeyValuePair object belongs to the
     * supplied collection having the specified key value.
     *
     * @param key    The key of the KeyValue pair object to find.
     * @param source The collection of KeyValuePair objects to be searched for the specified key.
     * @return true if the KeyValuePair object is contained within the collection otherwise false.
     */
    public static boolean contains(Collection<KeyValuePair> source, Object key)
    {
        for (KeyValuePair keyValuePair : source)
        {
            if (key.equals(keyValuePair.getKey()))
                return true;
        }

        return false;
    }

    /**
     * Create a Map object which contains the key value pair data. An entry is created
     * for each KeyValuePair object the key and value mapping as expected.
     *
     * @param keyValuePairCollection The data to be placed in the map.
     * @return A Map containing the specified data.
     */
    public static Map createMap(Collection<KeyValuePair> keyValuePairCollection)
    {
        HashMap result = new HashMap();

        for (Map.Entry entry : keyValuePairCollection)
            result.put(entry.getKey(), entry.getValue());

        return result;
    }

    /**
     * Create a Set object which contains a key value pair for each entry in the map.
     *
     * @param keyValuePairMap The data to be placed in the set.
     * @return A Set of KeyValuePair objects.
     */
    public static SortedSet createSet(Map keyValuePairMap)
    {
        TreeSet result = new TreeSet();

        for (Iterator entries = keyValuePairMap.entrySet().iterator(); entries.hasNext(); )
        {
            Map.Entry entry = (Map.Entry)entries.next();

            result.add(new KeyValuePair(entry.getKey(), entry.getValue()));
        }

        return result;
    }

    /**
     * Replaces the key corresponding to this key value pair with the specified key.
     *
     * @param key new key to be stored in this key value pair.
     * @return old key corresponding to the key value pair.
     *         <p/>
     *         The Comparable interface is implemented at the key level. If this object
     *         is ever to be used in an environment where the Comparable interface
     *         is a necessity, the key must implement Comparable.
     */
    public synchronized K setKey(K key)
    {
        K prevKey;

        prevKey = this.key;
        this.key = key;

        return prevKey;
    }

    /**
     * Replaces the value corresponding to this key value pair with the specified value.
     *
     * @param value new value to be stored in this key value pair.
     * @return old value corresponding to the key value pair.
     */
    public synchronized V setValue(V value)
    {
        V prevValue;

        prevValue = this.value;
        this.value = value;

        return prevValue;
    }

    /**
     * Returns a string representation of this key value pair. The string representation consists of the key
     * followed by a ": " string followed by the value.
     *
     * @return a string representation of this key value pair.
     */
    public synchronized String toString()
    {
        return key.toString() + ": " + value.toString();
    }

    /**
     * Compares this object with the specified object for order. Returns a negative integer,
     * zero, or a positive integer as this object is less than, equal to, or greater than the
     * specified object.<br>
     * This implementation compares key values.
     *
     * @param other the KeyValuePair to be compared.
     * @return a negative integer, zero, or a positive integer as this object is less than,
     *         equal to, or greater than the specified object.
     */
    public int compareTo(KeyValuePair<K, V> other)
    {
        return ((Comparable)key).compareTo(other.getKey());
    }
}
