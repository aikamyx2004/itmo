module Main (
  main
  ) where

import HW5.Evaluator
import HW5.Parser
import HW5.Pretty

-- | Read-eval-print loop for the "hi" interpreter
repl :: IO ()
repl = do
  putStr "hi> "
  input <- getLine
  case parse input of
    Left err -> putStrLn $ "Parse error: " ++ show err
    Right parsedExpr -> do
      case eval parsedExpr of
        Left _     -> putStrLn "Evaluation error: " 
        Right tValue -> case tValue of
            Left err -> putStrLn $ "Evaluation error: " ++ show err
            Right value -> print (prettyValue value)
            
            
  repl

main :: IO ()
main = repl
