package com.jwnba24.database_parse_project.cpabe.bswabe;

import it.unisa.dia.gas.jpbc.Element;

public class BswabePolynomial {
	int deg;
	/* coefficients from [0] x^0 to [deg] x^deg */
	Element[] coef; /* G_T (of length deg+1) */
}
