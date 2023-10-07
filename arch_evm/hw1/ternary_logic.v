module not_switch(in, out);
  input in;
  output out;
  
  supply1 power;
  supply0 ground;
  
  pmos (out, power, in);
  nmos (out, ground, in);
endmodule

module nand_switch(a, b, out);
  input a, b;
  output out;
  wire w;
  
  supply1 power;
  supply0 ground;
  
  nmos n1(w, ground, b);
  nmos n2(out, w, a);
  pmos p1(out, power, a);
  pmos p2(out, power, b);
endmodule

module and_switch(a, b, out);
  input a, b;
  output out;
  
  wire w;
  
  nand_switch my_nand(a, b, w);
  not_switch my_not(w, out);
  
endmodule

module nor_switch(a, b, out);
  input a, b;
  output out;
  wire w;
  
  supply1 power;
  supply0 ground;
  
  nmos n1(out, ground, a);
  nmos n2(out, ground, b);
  
  pmos p1(w, power, a);
  pmos p2(out, w, b);
endmodule

module or_switch(a, b, out);
  input a, b;
  output out;
  wire w;
  
  nor_switch my_nor(a, b, w);
  not_switch my_not(w, out);
endmodule

module xor_switch(a, b, out);
  input a, b;
  output out;
  wire not_a, not_b, and1, and2;
  not_switch my_nota(a, not_a);
  not_switch my_notb(b, not_b);
  and_switch my_and1(a, not_b, and1);
  and_switch my_and2(not_a, b, and2);
  or_switch my_or(and1, and2, out);
  
endmodule

module ternary_max(a0, a1, b0, b1, out0, out1);
  input a0, a1, b0, b1;
  output out0, out1;
  wire or0, and0;
  
  or_switch my_or0(a0, b0, or0);
  or_switch my_or1(a1, b1, out1);
  and_switch my_and(or0, out1, and0);
  xor_switch my_xor(and0, or0, out0);
  
endmodule

module ternary_consensus(a0, a1, b0, b1, out0, out1);
  input a0, a1, b0, b1;
  output out0, out1;
  wire or0, nor0, nand0, nand1;
  
  or_switch my_or(a1, b1, or0);
  nor_switch my_nor(a0, b0, nor0);
  nand_switch my_nand0(a1, b1, nand0);
  
  not_switch my_not(nand0, out1);
  nand_switch my_nand1(nand0, or0, nand1);
  
  nand_switch my_nand2(nand1, nor0, out0);
endmodule

module ternary_min(a0, a1, b0, b1, out0, out1);
  input a0, a1, b0, b1;
  output out0, out1;

endmodule

module ternary_any(a0, a1, b0, b1, out0, out1);
  input a0, a1, b0, b1;
  output out0, out1;

endmodule