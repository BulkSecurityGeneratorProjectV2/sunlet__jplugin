package test.net.jplugin.common.kits;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;

public class TestStream {
	// �м������
	// filter()�� ��Ԫ�ؽ��й���
	// sorted()����Ԫ������
	// map()��Ԫ��ӳ��
	// distinct()��ȥ���ظ���Ԫ��

	// ���ղ�����
	// forEach()������ÿ��Ԫ�ء�
	// reduce()����Stream Ԫ��������������磬�ַ���ƴ�ӣ���ֵ�� sum��min��max ��average ��������� reduce��
	// collect()������һ���µļ��ϡ�
	// min()���ҵ���Сֵ��
	// max()���ҵ����ֵ��
	public void test() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(3);
		list.add(4);
		list.add(3);
		list.add(4);

		//
		System.out.println("Test1 sorta.....");
		Optional<Integer> result = list.stream().sorted((c1,c2)->(c1-c2)).reduce((t,u)->t+u);
		System.out.println(result.orElse(100));
		
		System.out.println("Test2.....");
		//lamda��������ʱ����Ҫ������,���⣬����ð�ű�ʾ��
		list.stream().sorted((c1, c2) -> c1 - c2)
		.forEach(System.out::println);

		System.out.println("Test3.....");
		//lamdaһ������ʱ�򣬿��Բ�������
		list.stream().sorted((c1, c2) -> (c1-c2))
		.forEach(c->System.out.println(c));

		System.out.println("Test4 ������......");
		//��������Ҫ�ô����ţ������Ҫreturn�������һ����Ҫ
		list.stream().sorted((c1, c2) -> (c1-c2))
		.forEach((c) -> {System.out.println(c+Thread.currentThread().getName());try {
			Thread.sleep(1);
		} catch (Exception e) {
			e.printStackTrace();
		}});
	}

	public static void main(String[] args) {
		new TestStream().test();
	}
}
