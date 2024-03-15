module HW5.Pretty
  ( prettyValue
  ) where

import Data.Ratio
import Data.Scientific (fromRationalRepetendUnlimited, formatScientific, FPFormat (Fixed))
import HW5.Base
import Prettyprinter (Doc, pretty, slash)
import Prettyprinter.Render.Terminal (AnsiStyle)

prettyValue :: HiValue -> Doc AnsiStyle
prettyValue (HiValueNumber n)   = prettyRational n
prettyValue (HiValueFunction f) = prettyFunction f


prettyRational :: Rational -> Doc AnsiStyle
prettyRational r
  | re == 0 = pretty q
  | otherwise =
    case fromRationalRepetendUnlimited r of
      (_, Just _) -> prettyNonZero
      (n, Nothing) -> pretty (formatScientific Fixed Nothing n)
  where
    num = numerator r
    den = denominator r
    (q, re) = quotRem num den
    link = if q > 0 then " + " else " - "

    prettyNonZero
      | q == 0 = pretty re <> slash <> pretty den
      | otherwise = pretty q <> pretty link <> pretty (abs re) <> slash <> pretty den

prettyFunction :: HiFun -> Doc ann
prettyFunction HiFunDiv = pretty "div"
prettyFunction HiFunMul = pretty "mul"
prettyFunction HiFunAdd = pretty "add"
prettyFunction HiFunSub = pretty "sub"