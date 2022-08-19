package com.wuguangxin.utils;

/**
 * 排序工具类
 * Created by wuguangxin on 2020-05-29.
 */
public class SortUtils {

    /**
     * 冒泡排序
     *
     * @param arr 数组
     */
    public static int[] bubbleSort(int[] arr) {
        // 比较轮数
        for (int i = 0; i < arr.length; i++) {
            // 比较次数
            for (int j = 0; j < arr.length - 1 - i; j++) {
                // 如果比后一个大，则交换位置
                if (arr[j] > arr[j + 1]) {
                    // 交换数据
                    int temp = arr[j]; // 取出临时存放
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        return arr;
    }


    /**
     * 冒泡排序
     *
     * @param arr int数组
     * @return 排序后的数组
     */
    public static int[] bubbleSortAndReturn(int[] arr) {
        for (int x = 0; x < arr.length - 1; x++) {
            //-x:让每一次比较的元素减少。-1:避免角标越界
            for (int y = 0; y < arr.length - x - 1; y++) {
                if (arr[y] > arr[y + 1]) {
                    bubbleSortSwap(arr, y, y + 1);
                }
            }
        }
        return arr;
    }

    /**
     * 冒泡排序的置换函数
     *
     * @param arr 原数组
     * @param a   a
     * @param b   b
     */
    private static void bubbleSortSwap(int[] arr, int a, int b) {
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }

    /**
     * 快速排序（是对冒泡排序的改进）
     * 思想：
     * 通过一趟排序将要排序的数据分割成独立的两部分，
     * 其中一部分的所有数据都比另外一部分的所有数据都要小，
     * 然后再按此方法对这两部分数据分别进行快速排序，
     * 整个排序过程可以递归进行，以此达到整个数据变成有序序列。
     *
     * @param arr   数组
     * @param start 起始位 0
     * @param end   结束位 数组长度-1（arr.length-1）
     */
    public static void quickSort(int[] arr, int start, int end) {
        if (start < end) {
            // 把数组中的第0位作为基准数
            int stand = arr[start];
            // 记录需要排序的下标
            int low = start;
            int high = end;
            // 循环找比基准数大的数和比基准数小的数
            while (low < high) {
                // 右边的数字比基准数大
                while (low < high && stand <= arr[high]) {
                    high--;
                }
                // 使用右边的数字代替左边的数
                arr[low] = arr[high];
                //如果左边的数比基准数小
                while (low < high && arr[low] <= stand) {
                    low++;
                }
                arr[high] = arr[low];
            }
            // 把基准数赋给low所在的位置的元素
            arr[low] = stand;
            // 处理所有小的数字
            quickSort(arr, start, low);
            // 处理所有大的数字
            quickSort(arr, low + 1, end);
        }
    }

    /**
     * 插入排序
     * 缺点：如果最后一个数比较小，那么效率会很慢（希尔排序正是解决这一缺点）
     *
     * @param arr
     */
    public static void insertSort(int[] arr) {
        // 遍历所有数字
        for (int i = 1; i < arr.length; i++) {
            // 如果当前数字比前一个数字小
            if (arr[i] < arr[i - 1]) {
                // 把当前遍历数字保存起来
                int temp = arr[i];
                int j;
                // 遍历当前数字前面所有的数字
                for (j = i - 1; j >= 0 && temp < arr[j]; j--) {
                    // 把前一个数字赋给后一个数字
                    arr[j + 1] = arr[j];
                }
                // 把临时遍历（外层for循环的当前元素）赋给不满足条件的后一个。
                arr[j + 1] = temp;
            }
        }
    }

    /**
     * 希尔排序
     *
     * @param arr
     */
    public static void shellSort(int[] arr) {
        // 遍历所有的步长（第一步是len/2, 第二步len/2/2 第三步len/2/2/2 ...）
        for (int d = arr.length / 2; d > 0; d /= 2) {
            // 遍历所有元素
            for (int i = d; i < arr.length; i++) {
                // 遍历本组所有元素
                for (int j = i - d; j >= 0; j -= d) {
                    // 如果当前元素大于加上步长后的元素，则交换位置
                    if (arr[j] > arr[j + d]) {
                        int temp = arr[j];
                        arr[j] = arr[j + d];
                        arr[j + d] = temp;
                    }
                }
            }
        }
    }

    /**
     * 选择排序
     *
     * @param arr 数组
     */
    public static void selectSort(int[] arr) {
        // 遍历所有元素
        for (int i = 0; i < arr.length; i++) {
            // 把当前遍历的数和后面所有的数依次进行对比，并记录下最屌的数的下标
            int minIndex = i;
            for (int j = i + 1; j < arr.length; j++) {
                // 如果后面比较的数比记录的最小的数小
                if (arr[minIndex] > arr[j]) {
                    // 记录下最小的数的下标
                    minIndex = j;
                }
            }
            // 如果最小的数和当前遍历的数的下标不一致，说明下标为minIndex的数比当前遍历的数小。
            if (i != minIndex) {
                int temp = arr[i];
                arr[i] = arr[minIndex];
                arr[minIndex] = temp;
            }
        }
    }

    /**
     * 归并排序
     *
     * @param arr
     */
//    public static void megerSort(int[] arr) {
//
//    }

    /**
     * 奇数排序
     *
     * @param arr
     */
//    public static void oddSort(int[] arr) {
//
//    }

    /**
     * 奇数排序（队列实现）
     *
     * @param arr
     */
//    public static void queueOddSort(int[] arr) {
//        // TODO 未实现
//    }

    /**
     * 堆排序
     *
     * @param arr
     */
    public static void heapSort(int[] arr) {
        // [9, 6, 8, 7, 0, 1, 10, 4, 2]
        int start = (arr.length - 1) / 2;
        for (int i = start; i >= 0; i--) {
            maxHeap(arr, arr.length, i);
        }
        // 现在已得到最大的数在最前面
        // [10, 7, 9, 6, 0, 1, 8, 4, 2]
        for (int i = arr.length - 1; i > 0; i--) {
            // 先把数组中的第0个和堆中的最后一个数交换位置
            int temp = arr[0];
            arr[0] = arr[i];
            arr[i] = temp;
            // 再把前面的处理为大顶堆
            maxHeap(arr, i, 0);
        }
    }

    /**
     * 调整为大顶堆
     *
     * @param arr   数组
     * @param size  元素长度
     * @param index 开始位置
     */
    public static void maxHeap(int[] arr, int size, int index) {
        // 左子节点
        int leftNode = 2 * index + 1;
        // 右子节点
        int rightNode = 2 * index + 2;
        // 默认最大节点为index
        int max = index;
        // 分别和两个子节点对比，找出最大的节点
        if (leftNode < size && arr[leftNode] > arr[max]) {
            max = leftNode;
        }
        if (rightNode < size && arr[rightNode] > arr[max]) {
            max = rightNode;
        }
        // 交换位置
        if (max != index) {
            int temo = arr[index];
            arr[index] = arr[max];
            arr[max] = temo;
            // 交换位置后，可能会破坏之前排好的堆，所有之前排好的堆需要重新调整
            maxHeap(arr, size, max);
        }
    }

}
