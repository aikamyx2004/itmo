`include "alu.v"
`include "control_unit.v"
`include "util.v"

module mips_cpu(clk, instruction_memory_a, instruction_memory_rd, data_memory_a, data_memory_rd, data_memory_we, data_memory_wd,
                register_a1, register_a2, register_a3, register_we3, register_wd3, register_rd1, register_rd2);
  input clk;
  output data_memory_we;
  output wire [31:0] instruction_memory_a, data_memory_a, data_memory_wd;
    inout [31:0] instruction_memory_rd, data_memory_rd;
  output register_we3;
  output wire [4:0] register_a1, register_a2, register_a3;
  output [31:0] register_wd3;
  inout [31:0] register_rd1, register_rd2;
  wire reg [31:0] d, q;
  wire reg [31:0] pcplus4, pcbranch; 
  reg [31:0] four = 4;
  wire reg [31:0] sign_imm, shift_imm, srca, srcb, aluresult, result;
  wire reg memtoreg, memwrite, branch, alusrc, regdst, regwrite, zero, pcsrc;
  wire reg [2:0] alucontrol;
  wire reg [4:0] writereg;
  
  adder add4(four, q, pcplus4);
  sign_extend ext(instruction_memory_rd[15:0], sign_imm);
  shl_2 shift(sign_imm, shift_imm);
  adder add_branch(shift_imm, pcplus4, pcbranch);
  mux2_32 mux_d(pcplus4, pcbranch, pcsrc, d);
  d_flop flop(d, clk, q);
    
 
  control_unit unit(instruction_memory_rd[31:26],instruction_memory_rd[5:0], memtoreg, memwrite, branch, alusrc, regdst, regwrite, alucontrol);
  
  assign register_we3 = regwrite;
  assign instruction_memory_a = q;
  assign register_a1 = instruction_memory_rd[25:21];
  assign register_a2 = instruction_memory_rd[20:16];
  
  mux2_5 mux_writereg(instruction_memory_rd[20:16], instruction_memory_rd[15:11], regdst, writereg);
  assign register_a3 = writereg;
  
  mux2_32 mux_res(aluresult, data_memory_rd, memtoreg, result);
  assign register_wd3 = result;
  
  always @ (data_memory_wd) begin
//     $monitor("%b", data_memory_wd);
  end
  assign data_memory_we = memwrite;
  assign data_memory_wd = register_rd2 ? register_rd2 : 0;
  assign data_memory_a = aluresult;
  assign srca = register_rd1 ? register_rd1 : 0;
  mux2_32 mux_srcb(register_rd2, sign_imm, alusrc, srcb);
  alu alu1(srca, srcb, alucontrol, aluresult, zero);
  
  assign pcsrc = branch & zero;
  
endmodule