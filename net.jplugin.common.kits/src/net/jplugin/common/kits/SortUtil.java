package net.jplugin.common.kits;

import java.util.List;



/**
 * @author LiuHang
 *
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
public class SortUtil {

	/**
	 * ð������ı����������Խ�һ���Ż���
	 * nxn �����Ż�Ϊ nxn/2
	 * @param arr
	 * @param comparor
	 */
	public static void sort(Object[] arr,Comparor comparor){
		Object tmp;
		for (int i=0;i<arr.length-1;i++){
			for (int j=0;j<arr.length-1;j++){
				if (comparor.isGreaterThen(arr[j],arr[j+1])){
					tmp=arr[j];
					arr[j]=arr[j+1];
					arr[j+1]=tmp;
				}
			}
		}
	}

	/**
	 * @param pluginList
	 * @param comparor
	 */
	public static void sort(List list, Comparor comparor) {
		//Ŀǰ����һ�����ٵ�ʵ�֣��������Ż�
		Object[] data = new Object[list.size()];
		for (int i=0;i<list.size();i++){
			data[i] = list.get(i);
		}
		list.clear();
		
		sort(data,comparor);
		
		for (Object elem:data){
			list.add(elem);
		}
		
	}
}
