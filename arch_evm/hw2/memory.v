module data_memory(a, we, clk, wd, rd);
  input we, clk;
  input [31:0] a;
  input [31:0] wd;
  output wire[31:0] rd;
  reg[10:0] i;
  reg [31:0] adress[63:0];
  initial begin
    for(i = 0; i < 64; i = i+1) begin
      adress[i] = 0;
    end
  end
  always @ (posedge clk) begin
    if(we) begin
      adress[a/4] = wd;
    end
  end
  assign rd =  adress[a/4];
   
endmodule

module instruction_memory(a, rd);
  input [31:0] a;
  output reg[31:0] rd;

  // Note that at this point our programs cannot be larger then 64 subsequent commands.
  // Increase constant below if larger programs are going to be executed.
  reg [31:0] ram[63:0];

  initial
    begin
      $readmemb("instructions.dat", ram);
    end
	
  always @ (a) begin
    rd = ram[a/4];
  end
endmodule