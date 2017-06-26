package com.indoorloc.model;
/**���Ƕ�λ
 * a(ax,ay),b(bx,by),c(cx,cy)�ֱ�Ϊ�û�ԭ����ת�����ĵ��ĵ���a��b��c������
 * p��Ϊ�û�λ�ã�apb��bpc�������û��ڶ�Ӧ�̵�����ת��<������>
 * */
public class TrianglePosition {
	private Point a;
	private Point b;
	private Point c;
	private double apb;
	private double bpc;
	
	public TrianglePosition(Point _a, Point _b, Point _c, double _apb, double _bpc){
		a = _a;
		b = _b;
		c = _c;
		apb = _apb;
		bpc = _bpc;
	}
	
	public Point getUserPosition() {
		double ax = a.x;
		double ay = a.y;
		double bx = b.x;
		double by = b.y;
		double cx = c.x;
		double cy = c.y;
		
		double bc = Math.sqrt(Math.pow(cx-bx,2)+Math.pow(cy-by,2));
		double ab = Math.sqrt(Math.pow(ax-bx,2)+Math.pow(ay-by,2));
		// ԭthetaֵ<������>�Ƶ���ʽ������������ȷ��ʽ
		double theta = Math.acos(((cx-bx)*(ax-bx)+(cy-by)*(ay-by))/(bc*ab));
		// �м�ֵ����
		double cot_bpc = Math.cos(bpc)/Math.sin(bpc);
		double sin_apb_theta = Math.sin(apb+theta);
		double cos_apb_theta = Math.cos(apb+theta);
		double sin_apb = Math.sin(apb);
		double num1 = bc*ab*(sin_apb_theta*cot_bpc+cos_apb_theta);
		double denominator = Math.pow(ab*sin_apb_theta-bc*sin_apb, 2)
				+Math.pow(ab*cos_apb_theta+bc*sin_apb*cot_bpc, 2);
		
		double x0 = num1*(bc*sin_apb*cot_bpc+ab*cos_apb_theta)/denominator;
		double y0 = num1*(ab*sin_apb_theta-bc*sin_apb)/denominator;
		
		//�û�����
		double xp = x0*(cx-bx)/bc-y0*(cy-by)/bc+bx;
		double yp = x0*(cy-by)/bc+y0*(cx-bx)/bc+by;
		int x = (new Double(xp)).intValue();
		int y = (new Double(yp)).intValue();
		
		Point user = new Point(x, y);
		return user;
	}
	
	public String getUserPositionStr() {
		Point userPosition = getUserPosition();
		String res = userPosition.x + "|" + userPosition.y;
		return res;
	}
}
