package net.jplugin.core.das.hib.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Entity {
	public String tableName() default "";
	public String idField() default "id";
	public String[] indexes() default "";
	public String idgen() default "native"; 
	public String textFields() default "";
	//native���������ݿ�,�������ݿ��Զ�ȷ����|uuid.hex(uuid)|assigned|increment(�ڴ棬��֧�ֶ�ʵ��)|sequence(db2,ora)|identity����������oracle��֧�֣�
}
