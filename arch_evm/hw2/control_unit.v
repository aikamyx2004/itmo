module control_unit(opcode, funct, memtoreg, memwrite, branch, alusrc, regdst, regwrite, alucontrol);
  input [5:0] opcode, funct;
  output reg memtoreg, memwrite, branch, alusrc, regdst, regwrite;
  output reg [2:0] alucontrol;
  always @ (opcode or funct) begin
    case(opcode)
      6'b100011: begin
        //lw
        regdst = 0;
        regwrite = 1;
        alusrc = 1;
        memwrite = 0;
        memtoreg = 1;
        branch = 0; 
        alucontrol = 3'b010;
      end
      6'b001000: begin
        //ADDI
        regwrite = 1;  
        regdst = 0;  
        alusrc = 1;  
        branch = 0;  
        memwrite = 0;  
        memtoreg = 0;  
        alucontrol = 3'b010;  
      end
      6'b101011: begin
        //sw
        regdst = 0;
        regwrite = 0;
        alusrc = 1;
        memwrite = 1;
        memtoreg = 0;
        branch = 0; 
      	alucontrol = 3'b010;
      end
      6'b000100: begin
        //beq
        regdst = 0;
        regwrite = 0;
        alusrc = 0;
        memwrite = 0;
        memtoreg = 0;
        branch = 1; 
      	alucontrol = 3'b110;
      end
      6'b000000: begin
        //operation
        regdst = 1;
        regwrite = 1;
        alusrc = 0;
        memwrite = 0;
        memtoreg = 0;
        branch = 0; 
        case(funct)
          6'b100000: alucontrol = 3'b010;//add
          6'b100010: alucontrol = 3'b110;//sub
          6'b100100: alucontrol = 3'b000;//and
          6'b100101: alucontrol = 3'b001;//or
          6'b101010: alucontrol = 3'b111;//slt
        endcase
        end
    endcase
  end
  // TODO: implementation
endmodule