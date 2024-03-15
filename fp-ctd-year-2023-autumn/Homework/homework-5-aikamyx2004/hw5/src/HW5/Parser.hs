module HW5.Parser
  ( parse,
  )
where

import Data.Void (Void)
import HW5.Base
import Text.Megaparsec (MonadParsec (eof, try), Parsec, between, many, runParser, sepBy, (<|>))
import Text.Megaparsec.Char (char, space, string)
import qualified Text.Megaparsec.Char.Lexer as L
import Text.Megaparsec.Error (ParseErrorBundle)
import Data.Foldable

type Parser = Parsec Void String

parse :: String -> Either (ParseErrorBundle String Void) HiExpr
parse = runParser (parseHiExpr <* space <* eof) ""

parseHiExpr :: Parser HiExpr
parseHiExpr = try parseHiExprApply <|> try (parens parseHiExpr) <|> parseHiExprValue

parseHiExprValue :: Parser HiExpr
parseHiExprValue = HiExprValue <$> parseHiValue

parseHiValue :: Parser HiValue
parseHiValue = try parseHiValueNumber <|> parseHiValueFunction


parseHiValueNumber :: Parser HiValue
parseHiValueNumber = HiValueNumber <$> fmap toRational (skipWs (L.signed space L.scientific))

parseHiValueFunction :: Parser HiValue
parseHiValueFunction = HiValueFunction <$> parseHiFun

parseHiFun :: Parser HiFun
parseHiFun =
    skipWs
    (   HiFunDiv            <$ string "div"
    <|> HiFunMul            <$ string "mul"
    <|> HiFunAdd            <$ string "add"
    <|> HiFunSub            <$ string "sub"
    )


parseArgs :: Parser [HiExpr]
parseArgs =  parseHiExpr `sepBy` comma

parseHiExprApply :: Parser HiExpr
parseHiExprApply = do
    fun <- skipWs ( HiExprValue <$> parseHiValue)
    args <- many (parens parseArgs)
    return (foldl' HiExprApply fun args)


skipWs :: Parser a -> Parser a
skipWs p = space *> p <* space

parens :: Parser a -> Parser a
parens = between openBracket closeBracket

openBracket :: Parser Char
openBracket = skipWs (char '(')

closeBracket :: Parser Char
closeBracket = skipWs (char ')')

comma :: Parser Char
comma = skipWs (char ',')