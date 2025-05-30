package io.github.burukeyou.dataframe.iframe;

import io.github.burukeyou.dataframe.iframe.function.*;
import io.github.burukeyou.dataframe.iframe.item.FI2;
import io.github.burukeyou.dataframe.iframe.window.Sorter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 * A Simple DataFrame Stream API Interface define
 *
 *
 * @author caizhihao
 */
public interface IFrame<T> extends ISummaryFrame<T>, IWhereFrame<T>, IJoinFrame<T>, IGroupFrame<T>, IOperationFrame<T>,Iterable<T>{


    /**
     * convert to data by Collector, same as stream Collector
     * @param collector     the {@code Collector} describing the reduction
     * @param <R>           the type of the result
     * @param <A>           the intermediate accumulation type of the {@code Collector}
     * @return              the result of the reduction
     */
    <R, A> R collect(Collector<? super T, A, R> collector);

    /**
     * Convert to list
     * @return      the list
     */
    List<T> toLists();

    /**
     * Convert to Array
     * @return      the Array， if Frame is empty will return null rather than empty array
     */
    T[] toArray();

    /**
     * Convert to Array
     * @param  elementClass         the array element class type
     * @return      the Array， Even if the Frame is empty, it will return an empty array instead of null
     */
    T[] toArray(Class<T> elementClass);

    /**
     * Convert to Map
     * @param keyMapper       a mapping function to produce keys
     * @param valueMapper     a mapping function to produce values
     */
    <K,V> Map<K,V> toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper);

    /**
     * Convert to Map
     * @param keyMapper       a mapping function to produce first keys
     * @param key2Mapper      a mapping function to produce second keys
     * @param valueMapper     a mapping function to produce values
     */
    <K,K2,V> Map<K,Map<K2,V>> toMulti2Map(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends K2> key2Mapper, Function<? super T, ? extends V> valueMapper);

    /**
     * Convert to Map
     * @param keyMapper       a mapping function to produce first keys
     * @param key2Mapper      a mapping function to produce second keys
     * @param key3Mapper      a mapping function to produce third keys
     * @param valueMapper     a mapping function to produce values
     */
    <K,K2,K3,V> Map<K,Map<K2,Map<K3,V>>> toMulti3Map(Function<? super T, ? extends K> keyMapper,
                                                     Function<? super T, ? extends K2> key2Mapper,
                                                     Function<? super T, ? extends K3> key3Mapper,
                                                     Function<? super T, ? extends V> valueMapper);


    /**
     * get stream
     * @return      the stream
     */
    Stream<T> stream();

    /**
     * Convert to other IFrame
     */
    <R> IFrame<R> from(Stream<R> data);

    /**
     * Convert to other IFrame
     */
    <R> IFrame<R> from(List<R> data);

    /**
     * Performs the given action for each element of the Iterable until all elements have been processed or the action throws an exception.
     */
    IFrame<T> forEachDo(Consumer<? super T> action);


    /**
     * Performs the given action for each element of the Iterable until all elements
     * have been processed or the action throws an exception.
     * the index starting from zero
     */
    IFrame<T> forEachIndexDo(ConsumerIndex<? super T> action);


    /**
     *  Iterate each current element and its previous elements to a specified function
     *  Note that if it is the first element, its preceding elements will be null
     */
    IFrame<T> forEachPreDo(ConsumerPrevious<? super T> action);

    /**
     *  Iterate each current element and its next elements to a specified function
     *  Note that if it is the last element, its next elements will be null
     */
    IFrame<T> forEachNextDo(ConsumerNext<? super T> action);


    /**
     * such as {@link #forEachDo(Consumer)} , but is parallel to forEach
     */
    IFrame<T> forEachParallel(Consumer<? super T> action);

    /**
     * Returns true if contains the specified element object
     * @param other         specified object
     */
    boolean isContains(T other);

    /**
     * return true if contain the value of the specified value of object
     * @param valueFunction     field value
     */
    <U> boolean isContainValue(Function<T,U> valueFunction, U value);

    /**
     * return true if not contain the value of specified value of object
     * @param valueFunction     field value
     */
    <U> boolean isNotContainValue(Function<T,U> valueFunction, U value);


    /**
     * To determine whether there is a null value, it is compatible with String type processing,
     * and if it is an empty string, it will return true
     * @param valueFunction          field value
     */
    <U> boolean hasNullValue(Function<T,U> valueFunction);

    /**
     * Equivalent to {@link java.util.stream.Stream#anyMatch}
     * @param predicate          a non-interfering, stateless predicate to apply to elements of this stream
     * @return                   true if any elements of the stream match the provided predicate, otherwise false
     */
    boolean anyMatch(Predicate<? super T> predicate);

    /**
     * Returns whether any elements of this frame match the specified value of object
     * @param valueFunction      field value
     * @param value              specified value
     */
    <U> boolean anyMatchValue(Function<T,U> valueFunction, U value);

    /**
     * Equivalent to {@link java.util.stream.Stream#allMatch}
     * @param predicate          a non-interfering, stateless predicate to apply to elements of this stream
     * @return                   true if either all elements of the stream match the provided predicate or the stream is empty, otherwise false
     */
    boolean allMatch(Predicate<? super T> predicate);

    /**
     * Returns whether all elements of this frame match the specified value of object
     * @param valueFunction      field value
     * @param value              specified value
     */
    <U> boolean allMatchValue(Function<T,U> valueFunction, U value);

    /**
     * Equivalent to {@link java.util.stream.Stream#noneMatch}
     * @param predicate          a non-interfering, stateless predicate to apply to elements of this stream
     * @return                   true if either no elements of the stream match the provided predicate or the stream is empty, otherwise false
     */
    boolean noneMatch(Predicate<? super T> predicate);

    /**
     * Returns whether no elements of this frame match the specified value of object
     * @param valueFunction      field value
     * @param value              specified value
     */
    <U> boolean noneMatchValue(Function<T,U> valueFunction, U value);

    /**
     * Concatenate the values of the fields according to the specified delimiter and  prefix ,suffix
     * @param joinField     splicing fields
     * @param delimiter     the delimiter to be used between each element
     * @param prefix        the sequence of characters to be used at the beginning of the joined result
     * @param suffix        the sequence of characters to be used at the end  of the joined result
     *
     */
    <U> String joining(Function<T,U> joinField,CharSequence delimiter, CharSequence prefix, CharSequence suffix);


    /**
     * Concatenate the values of the fields according to the specified delimiter
     * @param joinField     splicing fields
     * @param delimiter     the delimiter to be used between each element
     *
     */
    <U> String joining(Function<T,U> joinField,CharSequence delimiter);

    /**
     * Performs a reduction on the elements of this stream, using the provided identity value and an associative accumulation function, and returns the reduced value
     * @see Stream#reduce(Object, BinaryOperator)
     */
    T reduce(T identity, BinaryOperator<T> accumulator);

    /**
     * Performs a reduction on the elements of this stream, using an associative accumulation function, and returns an Optional describing the reduced value
     * @see  Stream#reduce(BinaryOperator)
     */
    T reduce(BinaryOperator<T> accumulator);

    /**
     * Performs a reduction on the elements of this stream, using the provided identity, accumulation and combining functions
     * @see Stream#reduce(Object, BiFunction, BinaryOperator)
     */
    <U> U reduce(U identity,
                 BiFunction<U, ? super T, U> accumulator,
                 BinaryOperator<U> combiner);

    /**
     * ===========================   Frame Setting =====================================
     **/
    /**
     * Set default decimal places
     */
    IFrame<T> defaultScale(int scale);

    /**
     *  Set default decimal places
     */
    IFrame<T> defaultScale(int scale, RoundingMode roundingMode);

    /**
     * ===========================   Frame Info =====================================
     **/
    /**
     * print the 15 row to the console
     *
     */
    void show();

    /**
     * print the n row to the console
     */
    void show(int n);

    /**
     *  Get column headers
     */
    List<String> columns();

    /**
     *  collect a column value to the list
     */
    <R> List<R> col(Function<T, R> function);

    /**
     *  collect a column value to the Set
     */
    <R> Set<R> colSet(Function<T, R> function);

    /**
     * Get paginated data
     * @param page              The current page number is considered as the first page, regardless of whether it is passed as 0 or 1
     * @param pageSize          page size
     */
    List<T> page(int page,int pageSize);

    /**
     * If the number of rows is 0, it is empty
     */
    boolean isEmpty();

    /**
     * If the number of rows is greater than 0, it is not empty
     */
    boolean isNotEmpty();


    /**
     * ===========================   Frame Convert  =====================================
     */
    /**
     * convert to the new Frame
     * @param map           convert operation
     * @return              the new Frame
     * @param <R>           the new Frame type
     */
    <R> IFrame<R> map(Function<T,R> map);

    /**
     * parallel convert  to the new Frame
     * @param map           convert operation
     * @return              the new Frame
     * @param <R>           the new Frame type
     */
    <R> IFrame<R> mapParallel(Function<T,R> map);

    /**
     * Percentage convert
     *          you can convert the value of a certain field to a percentage,
     *          Then assign a value to a certain column through SetFunction
     * @param get           need percentage convert field
     * @param set           field for storing percentage values
     * @param scale         percentage retain decimal places
     * @param <R>           the percentage field type
     */
    <R extends Number> IFrame<T> mapPercent(Function<T,R> get, SetFunction<T,BigDecimal> set, int scale);

    /**
     * Percentage convert
     *    such as {@link IFrame#mapPercent(Function, SetFunction, int)}, but default scale is 2
     * @param get           need percentage convert field
     * @param set           field for storing percentage values
     */
    <R extends Number> IFrame<T> mapPercent(Function<T,R> get, SetFunction<T,BigDecimal> set);

    /**
     * partition
     *      cut the matrix into multiple small matrices, with each matrix size n
     *
     * @param n         size of each zone
     */
    IFrame<List<T>> partition(int n);

    /**
     * add sort number to the {@link FI2#c2} field
     *      Default sequence number from 1 to frame.length
     */
    IFrame<FI2<T,Integer>> addRowNumberCol();

    /**
     * Sort by sorter first, then add ordinal columns
     * @param sorter    the sorter
     */
    IFrame<FI2<T,Integer>> addRowNumberCol(Sorter<T> sorter);

    /**
     * Add a numbered column to a specific column
     * @param set           specific column
     */
    IFrame<T> addRowNumberCol(SetFunction<T,Integer> set);

    /**
     * Add a numbered column to a specific column
     * @param sorter    the sorter
     * @param set           specific column
     */
    IFrame<T> addRowNumberCol(Sorter<T> sorter,SetFunction<T,Integer> set);

    /**
     * Add ranking columns by comparator
     *      Ranking logic, the same value means the Ranking is the same. This is different from {@link #addRowNumberCol}
     * @param sorter    the ranking  sorter
     */
    IFrame<FI2<T,Integer>> addRankCol(Sorter<T> sorter);


    /**
     * Add ranking column to a certain column by Comparator
     * @param sorter            the ranking  comparator
     * @param set                   certain column
     */
    IFrame<T> addRankCol(Sorter<T> sorter, SetFunction<T,Integer> set);

    /**
     *  Convert columns to multiple rows, to expand fields of arrays or complex types by element, generating multiple rows of data
     *  Cut the string into multiple lines according to the specified delimiter
     *  support string text format "a,b,c,d" or "[a,b,c,d]"
     *
     *
     * @param getFunction        wait to explode field
     * @param delimiter          split delimiter, support regex
     */
    IFrame<FI2<T,String>> explodeString(Function<T,String> getFunction, String delimiter);


    /**
     *  Convert columns to multiple rows, to expand fields of arrays or complex types by element, generating multiple rows of data
     *  Cut the string into multiple lines according to the specified delimiter
     *
     *
     * @param getFunction        wait to explode field
     * @param setFunction        accept the value after explode
     * @param delimiter          split delimiter, support regex
     */
    IFrame<T> explodeString(Function<T,String> getFunction, SetFunction<T,String> setFunction,String delimiter);

    /**
     *  Convert columns to multiple rows, to expand fields of arrays or complex types by element, generating multiple rows of data
     *  Cut the string into multiple lines according to the specified delimiter
     *
     *  Support explode field value type is JSON string array
     *
     * @param getFunction        wait to explode field
     */
    IFrame<FI2<T,String>> explodeJsonArray(Function<T,String> getFunction);


    /**
     *  Convert columns to multiple rows, to expand fields of arrays or complex types by element, generating multiple rows of data
     *  Cut the string into multiple lines according to the specified delimiter
     *
     *  Support explode field value type is JSON string array
     *
     * @param getFunction        wait to explode field
     * @param setFunction        accept the value after explode
     */
    IFrame<T> explodeJsonArray(Function<T,String> getFunction,SetFunction<T,String> setFunction);

    /**
     *  Convert columns to multiple rows, to expand fields of arrays or complex types by element, generating multiple rows of data
     *  Cut the string into multiple lines according to the specified delimiter
     *
     *  Support explode field value type is collection
     *
     * @param getFunction        wait to explode field
     */
    <E> IFrame<FI2<T,E>> explodeCollection(Function<T,? extends Collection<E>> getFunction);


    /**
     *  Convert columns to multiple rows, to expand fields of arrays or complex types by element, generating multiple rows of data
     *  Cut the string into multiple lines according to the specified delimiter
     *
     *  Support explode field value type is collection
     *
     * @param getFunction        wait to explode field
     * @param setFunction        accept the value after explode
     */
    <E> IFrame<T> explodeCollection(Function<T,? extends Collection<E>> getFunction,SetFunction<T,E> setFunction);

    /**
     *  Convert columns to multiple rows, to expand fields of arrays or complex types by element, generating multiple rows of data
     *  Cut the string into multiple lines according to the specified delimiter
     *
     *  Support explode field value type is array or collection
     *
     * @param getFunction        wait to explode field
     * @param elementClass       the array or collection element class
     */
    <E> IFrame<FI2<T,E>> explodeCollectionArray(Function<T,?> getFunction,Class<E> elementClass);


    /**
     *  Convert columns to multiple rows, to expand fields of arrays or complex types by element, generating multiple rows of data
     *  Cut the string into multiple lines according to the specified delimiter
     *
     *  Support explode field value type is array or collection
     *
     * @param getFunction        wait to explode field
     * @param setFunction        accept the value after explode
     * @param elementClass       the array or collection element class
     */
    <E> IFrame<T> explodeCollectionArray(Function<T,?> getFunction,SetFunction<T,E> setFunction,Class<E> elementClass);


    /**
     * ===========================   Sort Frame  =====================================
     **/

    /**
     * Descending order
     * @param comparator         comparator
     */
    IFrame<T> sortDesc(Comparator<T> comparator);

    /**
     * Descending order by field
     * @param function      sort field
     * @param <R>           the  sort field type
     */
    <R extends Comparable<? super R>> IFrame<T> sortDesc(Function<T, R> function);

    /**
     * sort by comparator
     * @param comparator         comparator
     */
    IFrame<T> sort(Sorter<T> comparator);

    /**
     * Ascending order
     * @param comparator         comparator
     */
    IFrame<T> sortAsc(Comparator<T> comparator);

    /**
     * Ascending order
     * @param function      sort field
     */
    <R extends Comparable<R>> IFrame<T> sortAsc(Function<T, R> function);


    /** ===========================   Cut Frame  ===================================== **/

    /**
     *  Cut the top n element
     * @param n    the top n
     */
    IFrame<T> cutFirst(int n);

    /**
     * Cut the last n element
     * @param n    the last n
     */
    IFrame<T> cutLast(int n);

    /**
     * cut elements within the scope
     */
    IFrame<T> cut(Integer startIndex,Integer endIndex);

    /**
     * cut paginated data
     * @param page              The current page number is considered as the first page, regardless of whether it is passed as 0 or 1
     * @param pageSize          page size
     */
    IFrame<T> cutPage(int page,int pageSize);


    /**
     * Cut the top N rankings data
     *          The same value is considered to have the same ranking
     * @param sorter                the ranking sorter
     * @param n                     the top n
     */
    IFrame<T> cutFirstRank(Sorter<T> sorter, int n);


    /** ===========================   View Frame  ===================================== **/

    /**
     * Get the first element
     */
    T head();

    /**
     * Get the first n elements
     */
    List<T> head(int n);

    /**
     * Get the last element
     */
    T tail();

    /**
     * Get the last n elements
     */
    List<T> tail(int n);

    /**
     *  Get elements within the scope.  [startIndex,endIndex]
     */
    List<T> getList(Integer startIndex, Integer endIndex);

    /** ===========================   Distinct Frame  ===================================== **/

    /**
     * distinct by  T value
     */
    IFrame<T> distinct();


    /**
     * distinct by field value
     * @param function          the field
     * @param <R>               field value type
     */
    <R extends Comparable<R>> IFrame<T> distinct(Function<T, R> function);

    /**
     * distinct by field value
     * @param function          the field
     * @param listOneFunction          When there are more than one repeated element, this method will be called back, and customization will determine which element to choose
     * @param <R>               field value type
     */
    <R extends Comparable<R>> IFrame<T> distinct(Function<T, R> function, ListSelectOneFunction<T> listOneFunction);


    /**
     * distinct by  comparator
     * @param comparator        the comparator
     */
    IFrame<T> distinct(Comparator<T> comparator);

    /**
     * distinct by  comparator
     * @param comparator        the comparator
     * @param function          When there are more than one repeated element, this method will be called back, and customization will determine which element to choose
     */
    IFrame<T> distinct(Comparator<T> comparator, ListSelectOneFunction<T> function);


    /** ===========================   Other  ===================================== **/

    /**
     * Summarize all collectDim values, calculate the difference between them,
     * and then add the missing difference to the Frame through getEmptyObject
     * Finally, return the list that needs to be supplemented
     *
     * @param  collectDim           Summary value
     * @param  allDim               all need value
     * @param  getEmptyObject       Generate a new object based on the difference using this function
     *
     */
    <C> List<T> replenishList(Function<T, C> collectDim, List<C> allDim, Function<C,T> getEmptyObject);


    /**
     * Summarize all collectDim values, calculate the difference between them,
     * and then add the missing difference to the Frame through getEmptyObject
     *
     * @param  collectDim           Summary value
     * @param  allDim               all need value
     * @param  getEmptyObject       Generate a new object based on the difference using this function
     */
    <C> IFrame<T> replenish(Function<T, C> collectDim, List<C> allDim, Function<C,T> getEmptyObject);


    /**
     * Calculate the difference in groups and then add the difference to that group
     *
     *  according to the groupDim dimension, and then summarize all collectDim fields within each group
     *  After summarizing, calculate the difference sets with allAbscissa, which are the entries that need to be supplemented.
     *  Then, generate empty objects according to the ReplenishFunction logic and add them to the group
     *
     * @param groupDim              Dimension fields for grouping
     * @param collectDim            Data fields collected within the group
     * @param allDim                All dimensions that need to be displayed within the group
     * @param getEmptyObject        Logic for generating empty objects
     *
     * @param <G>        The type of grouping
     * @param <C>        type of collection within the group
     *
     *The set supplemented by @ return
     */
    <G, C> IFrame<T> replenishGroup(Function<T, G> groupDim, Function<T, C> collectDim, List<C> allDim, ReplenishFunction<G,C,T> getEmptyObject);

    /**
     *  such as {@link IFrame#replenishGroup(Function, Function, List, ReplenishFunction)}, but can not Specify allDim，
     *  will auto generate allDim, The default allDim is the value of all collectDim fields in the set
     *
     * @param groupDim              Dimension fields for grouping
     * @param collectDim            Data fields collected within the group
     * @param getEmptyObject        Logic for generating empty objects
     *
     * @param <G>        The type of grouping
     * @param <C>        type of collection within the group
     */
    <G, C> IFrame<T> replenishGroup(Function<T, G> groupDim, Function<T, C> collectDim, ReplenishFunction<G,C,T> getEmptyObject);
}
