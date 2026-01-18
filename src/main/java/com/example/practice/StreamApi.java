package com.example.practice;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamApi {
	static class Product{
		private int id;
		private String name;
		private float cost;
		private String category;
		private double discount;
		
		public Product(int id, String name, float cost, String catgory, double discount) {
			this.id=id;
			this.name=name;
			this.cost=cost;
			this.category=catgory;
			this.discount=discount;
		}
		public int getid() {
			return id;
		}
		
		public void setid(int id) {
			this.id=id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name=name;
		}
		public float getCost() {
			return cost;
		}
		public void setCost(float cost) {
			this.cost=cost;
		}
		public String getCategory() {
			return category;
		}
		public void setCategory(String category) {
			this.category=category;
		}
		
		public double getDiscount() {
			return discount;
		}
		public void setDiscount(double discount) {
			this.discount=discount;
		}
		
		public String toString() {
			return String.format("Product{id=%d, name='%s', cost=%2f, category='%s', discount='%.2f%%'}", id,name,cost,category,discount);
		}
		
		
	}
public static void main(String[] args) {
	
	//product data
		List<Product> products = Arrays.asList(
			    new Product(1, "Laptop", 95000.00f, "Electronic", 10.0),
			    new Product(2, "iphone", 175000.00f, "Electronic", 8.0),
			    new Product(3, "fridge", 15000.00f, "Electronic", 17.0),
			    new Product(4, "Acconditionary", 25000.00f, "Electronic", 11.0),
		        new Product(5,"Allen solly shirt",1100.0f,"Clothes", 5.0),
		        new Product(6,"lenin shirt",1600.0f,"Clothes", 8.0),
		        new Product(7,"Raymond shirt",1250.0f,"Clothes", 8.0));
	
	
	//highesr cost
	List<Product> highcost=products.stream().filter(x->x.getCategory().equalsIgnoreCase("Electronic"))
	.filter(x->x.getCost()>15000).collect(Collectors.toList());
	System.out.println("highest cost greater than 15000"+highcost);

	//after discount amount each product related to Electronics
	products.stream().filter(x->x.getCategory().equalsIgnoreCase("Electronic"))
	.filter(x->x.getCost()>10000).forEach(p->{
		double finalamount=p.getCost()- (p.getCost()*p.getDiscount()/100);
		
		System.out.println(p.getName()+"->original:"+p.getCost()+",discount:"+p.getDiscount()+",finalamount:"+finalamount);
	});
	
	//after discount amount each product related to clothes
	products.stream().filter(x->x.getCategory().equalsIgnoreCase("Clothes")).filter(x->x.getCost()>1000).forEach(p->{
		double finalamountshirts=p.getCost()-(p.getCost()*p.getDiscount()/100);
		System.out.println(p.getName()+"->original:"+p.getCost()+",discount:"+p.getDiscount()+",finalamount:"+finalamountshirts);
	});
	
	
	
	
	List<Integer> li=Arrays.asList(5,12,81,23,15,32);
	// print max and min number in the list
	int max=li.stream().max(Integer::compare).get();
	int min=li.stream().min(Integer::compare).get();
	System.out.println(max);
	System.out.println(min);
	
	
	// print second largest and second lowest number
	
	int secondhighest=li.stream().sorted(Comparator.reverseOrder()).skip(1).findFirst().get();
	int secondlowest=li.stream().sorted().skip(1).findFirst().get();
	System.out.println("second highest number in the list="+secondhighest);
	System.out.println("second lowerst number in the list="+secondlowest);
	
	
	
	//print unique values in the list
	
	List<Integer> li2=Arrays.asList(10,20,20,30,40,40,50,50,60);
	List<Integer> uniquevaluesinthelist=li2.stream().distinct().collect(Collectors.toList());
	System.out.println(uniquevaluesinthelist);
	
	
	//Count how many times each number occurs in a list
	Map<Integer,Long> count=li2.stream().collect(Collectors.groupingBy(n->n,Collectors.counting()));
	System.out.println("frequency of the elements in the list"+count);
	
	//Calculate the sum of all numbers in a collection
	List<Integer> li3=Arrays.asList(5,15,20,25);
	int sum=li3.stream().mapToInt(Integer::intValue).sum();
	System.out.println("sum of the elements in the list="+sum);
	
	//Find the average value from a list of numbers
	double avg=li3.stream().mapToInt(Integer::intValue).average().getAsDouble();
	System.out.println("average of the elements in the lsit="+avg);
	
	//Extract all numbers greater than 10 from a list
	List<Integer> results=li3.stream().filter(n->n>10).collect(Collectors.toList());
	System.out.println(results);
	
	
	//Sort a list of integers in both ascending and descending order
	List<Integer> ascending=li3.stream().sorted().collect(Collectors.toList());
	List<Integer> descending=li3.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
	System.out.println("ascending order="+ascending);
	System.out.println("descending order="+descending);
	
	
	//Merge two lists of integers into one, ensuring there are no duplicates
	List<Integer> li4=Arrays.asList(1,2,3,4,6);
	List<Integer> li5=Arrays.asList(5,6,7,8,9);
	List<Integer> concated=Stream.concat(li4.stream(),li5.stream()).distinct().collect(Collectors.toList());
	System.out.println("concated the two list without duplicates values in the lsit"+concated);
	
	
	// From a list of integers, separate even and odd numbers into two different results
	List<Integer> li6=Arrays.asList(1,2,3,4,5,6,7,8,9);
	Map<Boolean,List<Integer>>seperateevenorodd=li6.stream().collect(Collectors.partitioningBy(n->n%2==0));
	System.out.println("even numbers"+seperateevenorodd.get(true));
	System.out.println("odd numbers"+seperateevenorodd.get(false));
	
	
	//Create a List of the square of all distinct numbers
	
	List<Integer> li7=Arrays.asList(3,9,11,12,1,2,3,9);
	List<Integer>squares=li7.stream().map(i->i*i).distinct().collect(Collectors.toList());
	System.out.println("sum of squares of the distinct elements in the list"+squares);
	
	
	
		

}
	
}

