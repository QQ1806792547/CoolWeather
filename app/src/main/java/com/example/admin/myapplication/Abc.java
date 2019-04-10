package com.example.admin.myapplication;

public class Abc {
    public static  int count = 0;//进过车辆数
    public static void main(String args[]){

        (new Thread(){
            @Override
            public void run() {
                System.out.println("hello world one!");
                while (true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    countCar();
//                    count++;
//                    System.out.println("又过了一辆车");
                }
            }
        }).start();

        (new Thread() {
          @Override
          public void run() {
//              System.out.println("hello world two!");
              while (true){
                  try {
                      Thread.sleep(1000);
                  } catch (InterruptedException e) {
                      e.printStackTrace();
                  }
                  printCar();
//                  if(count != 0){
//                      System.out.println("过去的车辆=> "+count);
//                      count = 0;
//                  }
              }
          }
        }).start();

//        for (int i=0; i<10; i++){
//            final int j = i;
//            (new Thread() {
//                @Override
//                public void run() {
//                    System.out.println(j);
//                }
//            }).start();
//        }
    }
    synchronized static void printCar(){
        if(count != 0){
              System.out.println("过去的车辆=> "+count);
              count = 0;
        }
    }
    synchronized static void countCar(){
        count++;
        System.out.println("又过了一辆车");
    }
}
