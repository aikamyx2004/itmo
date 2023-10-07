module alu(srca, srcb, alucontrol, aluresult, zero);
  input [31:0] srca, srcb;
  input [2:0] alucontrol;
  output reg[31:0] aluresult;
  output reg zero;
  reg [32:0] a, b, c;
  always @ (srca or srcb or alucontrol) begin
    a = srca;
    b = srcb;
    a[32] = a[31];
    b[32] = b[31];
    c = a-b;
    case(alucontrol)
      0:aluresult = srca & srcb;
      1:aluresult = srca | srcb;
      2:aluresult = srca + srcb;
      4:aluresult = srca & (~srcb);
      5:aluresult = srca | (~srcb);
      6:aluresult = srca - srcb;
      7:aluresult = c[32];
    endcase
    zero = aluresult == 0 ? 1'b1:1'b0;
  end
endmodule
