package com.xhg.utils.demo;

/**
 * @Author xiaoh
 * @create 2020/10/21 15:49
 */
public class JKtimeDemo {

    /**
     * 使用双层检查，要加上volatile
     */
    volatile JKtimeDemo jk = null;

    public JKtimeDemo getJKtimeDemo(){
        if(jk == null){
            synchronized (this){
                if(jk == null){
                    jk = new JKtimeDemo();
                }
            }
        }
        return jk;
    }

        public static void main(String[] args) {

        int[] arr = {2,15,17,7};
        System.out.println(twoSum(arr,9));



    }


    public static int JieCheng(int n){
         if(n == 1){
             return 1;
         }else {
             return n*JieCheng(n-1);
         }
     }

    public static String twoSum(int[] arr, int target){

        for (int i = 0; i < arr.length; i++){
            for (int j = 1; j < arr.length; j++){
                if(arr[i] + arr[j] == target){
                    return i +","+ j;
                }
            }
        }

        return null;
    }
}

