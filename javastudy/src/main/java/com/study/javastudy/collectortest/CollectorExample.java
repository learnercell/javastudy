package com.study.javastudy.collectortest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.study.javastudy.Programmer;
import com.study.javastudy.Util;

public class CollectorExample {
	
	public static Integer sum(List<Integer> list) {
		return  list.stream().collect(AtomicInteger::new, AtomicInteger::addAndGet, (ai1, ai2)-> ai1.addAndGet(ai2.get())).get();
	}
	
	public static <T> List<T> toList(Stream<T> stream) {
		return stream.collect(ArrayList<T>::new, List::add, List::addAll);
	}
	
	public static class Turple<L,R>{
		private final L l;
		private final R r;
		
		public Turple(L l, R r) {
			this.l = l;
			this.r = r;
		}

		@Override
		public String toString() {
			return "(" + l + ", " + r + " )";
		}
	}
	
	

	//key:language v:(key:level, [salary, list])
	public static Map<String, Map<Integer, Turple<Integer, List<Programmer>>>> classify(List<Programmer> programmers) {
		return programmers.stream().collect(
					Collectors.groupingBy(Programmer::getLanguage,//p -> p.getLanguage
													Collectors.groupingBy(Programmer::getLevel,
															Collectors.collectingAndThen(
																	Collectors.toList(),
																	list -> new Turple(list.stream().collect(Collectors.averagingInt(Programmer::getSalary)), list) 
																)
															)
								));
	}
	
	public static void main(String[] args) {
//		List<Integer> list = Arrays.asList(2,3,4,7,5);
//		System.out.println(sum(list));
		
		
		  List<Programmer> programmers = Util.generate(10);
		  System.out.println(classify(programmers));
	}
	

	
	
	
}
