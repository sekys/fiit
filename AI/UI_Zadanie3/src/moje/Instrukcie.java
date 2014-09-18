
package moje;

public class Instrukcie
{
	public static enum Type {
		INC, DEC, JUMP, PRINT
	}

	public static Type getType(byte data) {
		if (getBit(data, 7)) {
			return getBit(data, 6) ? Type.PRINT : Type.JUMP;
		}
		if(getBit(data, 6)) {
			return Type.DEC;
		}
		return Type.INC;
	}
	public static byte random() {
		int typ = (int) (Math.random() * 4);
		Type type = Type.INC;
		switch (typ) {
			case 0 : {
				type = Type.INC;
				break;
			}
			case 1 : {
				type = Type.DEC;
				break;
			}
			case 2 : {
				type = Type.JUMP;
				break;
			}
			case 3 : {
				type = Type.PRINT;
				break;
			}
		}
		byte value = (byte) (Math.random() * 64);
		return setType(value, type);
	}
	public static byte setType(byte data, Type typ) {
		switch (typ) {
			case INC : {
				data = setBit0(data, 7);
				data = setBit0(data, 6);
				break;
			}
			case DEC : {
				data = setBit0(data, 7);
				data = setBit1(data, 6);
				break;
			}
			case JUMP : {
				data = setBit1(data, 7);
				data = setBit0(data, 6);
				break;
			}
			case PRINT : {
				data = setBit1(data, 7);
				data = setBit1(data, 6);
				break;
			}
		}
		return data;
	}

	public static boolean getBit(byte data, int pos) {
		int x = (data >> pos) & 1;
		if(x == 1) return true;
		else if (x == 0) return false;
		throw new UnsupportedOperationException();		
		//return (data & (0x01 << pos)) == 1;
	}

	public static byte setBit1(byte data, int index) {
		return (byte) (data | (1 << index)); // 1
	}
	public static byte setBit0(byte data, int index) {
		return (byte) (data & ~(1 << index)); // 0
	}
	public static int getValue(byte data) {
		return extractBits(data, 6);
	}

	public static int extractBits(byte x, int n) {
		return x & ((1 << n) - 1);
	}

	public static byte copyValue(byte source, byte target) {
		for (int i = 0; i < 6; i++) {
			if (getBit(source, i)) {
				target = setBit1(target, i);
			} else {
				target = setBit0(target, i);
			}
		}
		return target;
	}

	public static int obsahujeJedniciek(byte source) {
		int pocet = 0;
		for (int i = 0; i < 8; i++) {
			if (getBit(source, i)) {
				pocet++;
			}
		}
		return pocet;
	}

	public static byte String2Byte(String txt) { // max pre 8
		char[] bits = txt.toCharArray();
		byte data = 0;
		for (int i = 0; i < 8; i++) {
			if (bits[7-i] == '1') {
				data |= 1 << i;
			}
		}
		return data;
	}
	
	public static String Byte2String(byte x) {
		String bytes = Integer.toBinaryString(x);
		int dlzka = bytes.length();
		return bytes.substring(Math.max(0, dlzka-8), dlzka);
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		byte data;
		
		data = String2Byte("01010000");
		Type typ = getType(data);
		
		data = String2Byte("11000101");
		System.out.println ( Byte2String(data) );
		int pocet = obsahujeJedniciek(data);
		
		return;
	}
}
