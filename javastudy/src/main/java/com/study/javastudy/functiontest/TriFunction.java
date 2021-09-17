package com.study.javastudy.functiontest;

@FunctionalInterface
public interface TriFunction<A,B,C,D>{
		D apply(A a, B b, C c);
	}