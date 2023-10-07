module register_file(clk, we3, a1, a2, a3, wd3, rd1, rd2);
  input clk, we3;
  input [4:0] a1, a2, a3;
  input [31:0] wd3;
  output reg [31:0] rd1, rd2;
  reg[31:0] q;
  reg[7:0] i;
//   reg[31:0] q;
  reg [31:0] adress[63:0];
  initial begin
    for(i=0; i<64; i=i+1) begin
      adress[i]=0;
    end
  end
  
  always @ (posedge clk) begin
    if(we3) begin
      adress[a3] = wd3;
      q = adress[a3];
//       $monitor("%b",q);
    end  
  end
  always @(a1) begin
    rd1 = a1 ? adress[a1] : 0;
  end
  always @(a2) begin
    rd2 = a2 ? adress[a2] : 0;
  end
  
  // TODO: implementation
endmodule