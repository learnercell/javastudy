package com.study.javastudy;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Util {
	public static <T,R> Function<T,R> S2F(Supplier<R> supplier) {
		return any -> supplier.get();
	}
	
	public static <R> Runnable S2R(Supplier<R> supplier) {
		return ()->supplier.get();
	}
	
	public static <T,R> Consumer<T> F2C(Function<T, R> fn) {
		return i -> fn.apply(i);
	}
	
//	public static <T,R> Function<T,R> safe(Function<T, R> fn) {
//		return i -> try{fn.apply(i)}catch
//	}//stream.map(safe(f))
	
	private static <T, INSTANCE> Consumer<INSTANCE> apply(BiConsumer<INSTANCE, T> consumer, Supplier<T> valueSupplier) {
		return instance ->consumer.accept(instance, valueSupplier.get());
	}
	
	public static <I,O> Supplier<O> apply(Function<I,O> fn, I arg){
		return ()-> fn.apply(arg);
	}
	
	private static <T> T randomPick(T[] possibleValues) {
		Random random = new Random();
		return possibleValues[random.nextInt(possibleValues.length)];
	}
	
	
	public static List<Programmer> generate(int count){
		Random random = new Random();
		return IntStream.rangeClosed(0, count)
				  .boxed()
				  .map(S2F(Programmer::new))// i -> new Programmer()
				  .peek(//Stream<Programmer>
					  apply(Programmer::setLevel,  apply(random::nextInt, 3))//p.setLevel(random.nextInt(3))
					  .andThen(apply(Programmer::setSalary, apply(random::nextInt, 50000)))
					  .andThen(apply(Programmer::setName, ()-> "Programmer" + random.nextInt(count*10)))
					  .andThen(apply(Programmer::setOutput, apply(random::nextInt, 10)))
					  .andThen(apply(Programmer::setLanguage, apply(Util::randomPick,new String[]{"JAVA","JS","Lisp", "Haskell"})))
				    )
				  //.peek(System.out::println)
				  .filter(p->p.getSalary() >=3000 && p.getLevel() > 0)
				  .sorted(Comparator.comparing(Programmer::getLevel))
				  .peek(System.out::println)
				  .collect(Collectors.toList())
				  ;
	}
	
	
	public static void main(String[] args) {
		System.out.print(generate(10));
	}
	
	public static <T,R> Function<Stream<T>, Stream<R>> wrapMap(Function<T,R> mapFn){
		return  stream -> stream.map(mapFn);	
	}
	public static <T,R> Function<Stream<T>, Stream<T>> wrapFilter(Predicate<T> p){
		return  stream -> stream.filter(p);
	}
	
	public static <T,R> Function<Stream<T>, Stream<R>> mapAndFilter(Function<T,R> mapFn, Predicate<R> p) {
		return stream -> wrapMap(mapFn).andThen(wrapFilter(p)).apply(stream);
	}
	
	public static <T,R>  Function<Optional, Optional>  opMap(Function<T, R> fn){
		return op -> op.map(fn);
	}
	
	
}
