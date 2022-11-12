package com.s8.io.bohr.neon.fields.arrays;

import java.io.IOException;

import com.s8.io.bohr.atom.BOHR_Types;
import com.s8.io.bohr.neon.core.BuildScope;
import com.s8.io.bohr.neon.core.NeObjectTypeHandler;
import com.s8.io.bohr.neon.fields.NeFieldValue;
import com.s8.io.bytes.alpha.ByteInflow;
import com.s8.io.bytes.alpha.ByteOutflow;


/**
 * 
 *
 * @author Pierre Convert
 * Copyright (C) 2022, Pierre Convert. All rights reserved.
 * 
 */
public class Float64ArrayNeFieldComposer extends PrimitiveNeFieldHandler {

	public final static long SIGNATURE =  BOHR_Types.ARRAY << 8 & BOHR_Types.FLOAT64;

	public @Override long getSignature() { return SIGNATURE; }


	public Float64ArrayNeFieldComposer(NeObjectTypeHandler prototype, String name) {
		super(prototype, name);
	}


	@Override
	public void publishEncoding(ByteOutflow outflow) throws IOException {
		outflow.putUInt8(BOHR_Types.ARRAY);
		outflow.putUInt8(BOHR_Types.FLOAT64);
	}
	

	
	/**
	 * 
	 * @param values
	 * @return
	 */
	public double[] get(NeFieldValue wrapper) {
		return ((Value) wrapper).value;
	}
	
	
	/**
	 * 
	 * @param values
	 * @param value
	 */
	public void set(NeFieldValue wrapper, double[] value) {
		((Value) wrapper).setValue(value);
	}
	
	
	
	@Override
	public NeFieldValue createValue() {
		return new Value();
	}

	

	
	
	/**
	 * 
	 * @author pierreconvert
	 *
	 */
	public static class Value extends PrimitiveNeFieldHandler.Value {
		
		private double[] value;
	
		public Value() {
			super();
		}
		
		public void setValue(double[] value) {
			this.value = value;
			this.hasDelta = true;
		}

		@Override
		public void compose(ByteOutflow outflow) throws IOException {
			if(value != null) {
				int length = value.length;
				outflow.putUInt7x(length);
				for(int i=0; i<length; i++) {
					outflow.putFloat64(value[i]);		
				}
			}
			else {
				outflow.putUInt7x(-1);
			}
		}

		@Override
		public void parse(ByteInflow inflow, BuildScope scope) throws IOException {
			int length = (int) inflow.getUInt7x();
			if(length >=0 ) {
				value = new double[length];
				for(int i=0; i<length; i++) {
					value[i] = inflow.getFloat64();
				}
			}
			else {
				value = null;
			}
		}
	}
}
