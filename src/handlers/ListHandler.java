package handlers;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ListHandler<T>
{
    public static final String VERSION = "1.0";
    public static final String AUTHOR = "P7ori [Pedro Sartori Dias dos Reis]";

    //FIELDS_____________________________________________________________________________________
    private List<T> list;

    //CONSTRUCTORS_______________________________________________________________________________
    public ListHandler()
    {
        list = new ArrayList<T>();
    }

    public ListHandler(List<T> list)
    {
        this.list = list;
    }

    //STATIC METHODS______________________________________________________________________________

    /*Creates a new instance of CollectionHandler<T> from an existing List<T>*/
    public static <T> ListHandler<T> fromList(List<T> list)
    {
        List<T> listT = new ArrayList<T>(list);
        return new ListHandler<T>(listT);
    }

    //INSTANCE METHODS____________________________________________________________________________

    /*Adds the given item to the collection*/
    public void add(T item)
    {
        list.add(item);
    }

    /*Adds the given items to the collection*/
    @SafeVarargs public final void add(T... items)
    {
        list.addAll(Arrays.asList(items));
    }

    /*Adds the given collection to the list*/
    public void join(Collection<? extends T> collection)
    {
        list.addAll(collection);
    }

    /*Adds the given ListHandler to the list*/
    public void join(ListHandler<? extends T> listHandler)
    {
        list.addAll(listHandler.toList());
    }

    /*Removes the given item to the collection*/
    public void remove(T item)
    {
        list.remove(item);
    }

    /*Removes the given collection to the list*/
    public void remove(Collection<? extends T> collection)
    {
        list.removeAll(collection);
    }

    /*Removes the given ListHandler to the list*/
    public void remove(ListHandler<? extends T> listHandler)
    {
        list.removeAll(listHandler.toList());
    }

    /*Removes from the list all the elements that meet the given condition*/
    public void removeIf(Predicate<T> predicate)
    {
        list.removeIf(predicate);
    }

    /*Removes the given items to the collection*/
    @SafeVarargs public final void remove(T... items)
    {
        list.removeAll(Arrays.asList(items));
    }

    /*Remove all repeated items from the list*/
    public void removeRepeated()
    {
        Set<T> setT = new LinkedHashSet<>(list);
        list = new ArrayList<>(setT);
    }

    /*Leaves in the list just the elements that match both collections*/
    public void intersect(Collection<? extends T> collection)
    {
        list.retainAll(collection);
    }

    /*Leaves in the list just the elements that match both collections*/
    public void intersect(ListHandler<? extends T> listHandler)
    {
        list.retainAll(listHandler.toList());
    }

    /*Clears the collection*/
    public void clear()
    {
        list.clear();
    }

    /*Converts the CollectionHandler<T> object to an object of the type List<T>*/
    public List<T> toList()
    {
        return new ArrayList<>(list);
    }

    /*Returns a new CollectionHandler object of type R containing new values based on a given function*/
    public <R> ListHandler<R> select(Function<T, R> selector)
    {
        return fromList(list.stream().map(selector).collect(Collectors.toList()));
    }

    /*Returns a new CollectionHandler<T> filtered by a given condition*/
    public ListHandler<T> where(Predicate<T> predicate)
    {
        return fromList(list.stream().filter(predicate).collect(Collectors.toList()));
    }

    /*Returns a new CollectionHandler<T> excluding the values that come before the given condition*/
    public ListHandler<T> startingWhere(Predicate<T> predicate)
    {
        List<T> listT = new ArrayList<>();
        boolean found = false;
        for(T t : list)
        {
            if(predicate.test(t))
                found = true;
            if(found)
                listT.add(t);
        }
        return fromList(listT);
    }

    /*Returns a new CollectionHandler<T> excluding the value that attends the given condition along
    with the values that come before it*/
    public ListHandler<T> startingAfter(Predicate<T> predicate)
    {
        List<T> listT = new ArrayList<>();
        boolean found = false;
        for(T t : list)
        {
            if(found)
                listT.add(t);
            if(predicate.test(t))
                found = true;
        }
        return fromList(listT);
    }

    /*Returns a new CollectionHandler<T> keeping the value that meets the given condition along with
    * the values that come before it*/
    public ListHandler<T> endingWhere(Predicate<T> predicate)
    {
        List<T> listT = new ArrayList<>();
        for(T t : list)
        {
            listT.add(t);
            if(predicate.test(t))
                break;
        }
        return fromList(listT);
    }

    /*Returns a new CollectionHandler<T> keeping only the values that come before the given condition*/
    public ListHandler<T> endingBefore(Predicate<T> predicate)
    {
        List<T> listT = new ArrayList<>();
        for(T t : list)
        {
            if(predicate.test(t))
                break;
            listT.add(t);
        }
        return fromList(listT);
    }

    /*Returns a new CollectionHandler<T> object ordered ascending by the selected key*/
    public <R extends Comparable<? super R>> ListHandler<T> orderAscendingBy(Function<T, R> keySelector)
    {
        List<T> listT = new ArrayList<>(list);
        listT.sort(Comparator.comparing(keySelector));
        return fromList(listT);
    }

    /*Returns a new CollectionHandler<T> object ordered descending by the selected key*/
    public <R extends Comparable<? super R>> ListHandler<T> orderDescendingBy(Function<T, R> keySelector)
    {
        List<T> listT = new ArrayList<>(list);
        listT.sort((o1, o2) -> -(keySelector.apply(o1).compareTo(keySelector.apply(o2))));
        return fromList(listT);
    }

    /*Returns the sum of all terms of the collection. Throws ClassCastException if type T
    does not extend Number*/
    public double sum()
    {
        double sum = 0.0;
        for(T t : list)
            sum += (double)t;

        return sum;
    }

    /*Returns the sum of all terms selected by the given function*/
    public double sum(Function<T, Number> selector)
    {
        List<Number> listE = list.stream().map(selector).collect(Collectors.toList());
        return (double) listE.stream().reduce(0.0, (n1, n2) -> (double)n1 + (double)n2);
    }

    /*Returns the average of all terms of the collection. Throws ClassCastException if type T
    does not extend Number*/
    public double average()
    {
        return sum()/list.size();
    }

    /*Returns the average of the terms selected by the given function*/
    public double average(Function<T, Number> selector)
    {
        List<Number> listE = list.stream().map(selector).collect(Collectors.toList());
        return sum(selector)/listE.size();
    }

    /*Returns the product of the multiplication of all terms of the collection.
    Throws ClassCastException if type T does not extend Number*/
    public double multiply()
    {
        double mult = 1.0;
        for(T t : list)
            mult *= (double) t;

        return mult;
    }

    /*Returns the product of the multiplication of all terms selected by the given function*/
    public double multiply(Function<T, Number> selector)
    {
        List<Number> listE = list.stream().map(selector).collect(Collectors.toList());
        return (double) listE.stream().reduce(1.0, (n1, n2) -> (double)n1 * (double)n2);
    }

    /*Returns the maximum number of the collection. Throws ClassCastException if type T
    does not extend Number*/
    public double max()
    {
        double max = (double) list.get(0);
        for(T t : list)
        {
            double n = (double) t;
            if(n > max)
                max = n;
        }

        return max;
    }

    /*Returns the element which function returns the maximum number of the elements of the list*/
    public T max(Function<T, Number> function)
    {
        T max = list.get(0);
        for(T t : list)
        {
            double n = (double) function.apply(t);
            if(n > (double)function.apply(max))
                max = t;
        }

        return max;
    }

    /*Returns the item that has more characters. Throws ClassCastException if type T is not String*/
    public String maxString()
    {
        String max = (String) list.get(0);
        for(T t : list)
        {
            String s = (String) t;
            if(s.length() > max.length())
                max = s;
        }

        return max;
    }

    /*Returns the element which function returns the String that contains more characters
    of the elements of the list*/
    public T maxString(Function<T, String> function)
    {
        T max = list.get(0);
        for(T t : list)
        {
            String n = function.apply(t);
            if(n.length() > function.apply(max).length())
                max = t;
        }

        return max;
    }

    /*Returns the minimum number of the collection. Throws ClassCastException if type T
    does not extend Number*/
    public double min()
    {
        double min = (double) list.get(0);
        for(T t : list)
        {
            double n = (double) t;
            if(n < min)
                min = n;
        }

        return min;
    }

    /*Returns the element which function returns the minimum number of the elements of the list*/
    public T min(Function<T, Number> function)
    {
        T min = list.get(0);
        for(T t : list)
        {
            double n = (double) function.apply(t);
            if(n < (double)function.apply(min))
                min = t;
        }

        return min;
    }

    /*Returns the item that has the fewer characters. Throws ClassCastException if type T is not String*/
    public String minString()
    {
        String min = (String) list.get(0);
        for(T t : list)
        {
            String s = (String) t;
            if(s.length() < min.length())
                min = s;
        }

        return min;
    }

    /*Returns the element which function returns the String that contains the fewer characters
    of the elements of the list*/
    public T minString(Function<T, String> function)
    {
        T min = list.get(0);
        for(T t : list)
        {
            String s = function.apply(t);
            if(s.length() < function.apply(min).length())
                min = t;
        }

        return min;
    }

    /*Returns true if the list contains any element that meets the given condition*/
    public boolean containsAny(Predicate<T> predicate)
    {
        for(T t : list)
        {
            if(predicate.test(t))
                return true;
        }
        return false;
    }

    /*Returns the element correspondent to the given index*/
    public T get(int index)
    {
        return list.get(index);
    }

    /*Returns the first element of the list*/
    public T first()
    {
        return list.get(0);
    }

    /*Returns the last element of the list*/
    public T last()
    {
        return list.get(list.size() -1);
    }

    /*Returns a list of anonymous objects containing 1 property selected by the given function*/
    public <A> ListHandler<_a1<A>> selectNew(Function<T, A> a)
    {
        List<_a1<A>> anons = new ArrayList<>();
        for(T t : list)
        {
            _a1<A> anon1 = new _a1<>();
            anon1.setA(a.apply(t));

            anons.add(anon1);
        }
        return fromList(anons);
    }

    /*Returns a list of anonymous objects containing 2 properties selected by the given functions*/
    public <A, B> ListHandler<_a2<A, B>> selectNew(Function<T, A> a, Function<T, B> b)
    {
        List<_a2<A, B>> anons = new ArrayList<>();
        for(T t : list)
        {
            _a2<A, B> anon2 = new _a2<>();
            anon2.setA(a.apply(t));
            anon2.setB(b.apply(t));

            anons.add(anon2);
        }
        return fromList(anons);
    }

    /*Returns a list of anonymous objects containing 3 properties selected by the given functions*/
    public <A, B, C> ListHandler<_a3<A, B, C>>
    selectNew(Function<T, A> a, Function<T, B> b, Function<T, C> c)
    {
        List<_a3<A, B, C>> anons = new ArrayList<>();
        for(T t : list)
        {
            _a3<A, B, C> anon3 = new _a3<>();
            anon3.setA(a.apply(t));
            anon3.setB(b.apply(t));
            anon3.setC(c.apply(t));

            anons.add(anon3);
        }
        return fromList(anons);
    }

    /*Returns a list of anonymous objects containing 4 properties selected by the given functions*/
    public <A, B, C, D> ListHandler<_a4<A, B, C, D>>
    selectNew(Function<T, A> a, Function<T, B> b, Function<T, C> c, Function<T, D> d)
    {
        List<_a4<A, B, C, D>> anons = new ArrayList<>();
        for(T t : list)
        {
            _a4<A, B, C, D> anon4 = new _a4<>();
            anon4.setA(a.apply(t));
            anon4.setB(b.apply(t));
            anon4.setC(c.apply(t));
            anon4.setD(d.apply(t));

            anons.add(anon4);
        }
        return fromList(anons);
    }

    /*Performs the given action for each element of the list*/
    public void forEach(Consumer<T> action)
    {
        list.forEach(action);
    }

    //OVERRIDE METHODS_______________________________________________________________________________

    /*Returns a string representation of the list*/
    @Override public String toString()
    {
        return list.toString();
    }

    //CLASSES_________________________________________________________________________________________

    public static class _a1<A>
    {
        //FIELDS__________________________________________________________________
        private A a;

        //CONSTRUCTORS____________________________________________________________
        public _a1()
        {
        }

        //PROPERTY METHODS________________________________________________________
        public A getA() {return a;}

        public void setA(A value) {a = value;}
    }

    public static class _a2<A, B> extends _a1<A>
    {
        //FIELDS__________________________________________________________________
        private B b;

        //CONSTRUCTORS____________________________________________________________
        public _a2()
        {
            super();
        }

        //PROPERTY METHODS________________________________________________________
        public B getB() {return b;}

        public void setB(B value) {b = value;}
    }

    public static class _a3<A, B, C> extends _a2<A, B>
    {
        //FIELDS__________________________________________________________________
        private C c;

        //CONSTRUCTORS____________________________________________________________
        public _a3()
        {
            super();
        }

        //PROPERTY METHODS________________________________________________________
        public C getC() {return c;}

        public void setC(C value) {c = value;}
    }

    public static class _a4<A, B, C, D> extends _a3<A, B, C>
    {
        //FIELDS__________________________________________________________________
        private D d;

        //CONSTRUCTORS____________________________________________________________
        public _a4()
        {
            super();
        }

        //PROPERTY METHODS________________________________________________________
        public D getD() {return d;}

        public void setD(D value) {d = value;}
    }
}
