module HW1.T2
  ( N (..),
    nplus,
    nmult,
    nsub,
    nFromNatural,
    nToNum,
    ncmp,
    nEven,
    nOdd,
    ndiv,
    nmod,
  )
where

import Control.Exception (ArithException (DivideByZero), throw)
import Numeric.Natural

data N = Z | S N
  deriving (Show)

nplus :: N -> N -> N
nplus x Z = x
nplus x (S y) = nplus (S x) y

nmult :: N -> N -> N
nmult _ Z = Z
nmult x (S y) = nplus x (nmult x y)

nsub :: N -> N -> Maybe N
nsub x Z = Just x
nsub Z _ = Nothing
nsub (S x) (S y) = nsub x y

ncmp :: N -> N -> Ordering
ncmp x y = case nsub x y of
  Nothing -> LT
  (Just Z) -> EQ
  _ -> GT

nFromNatural :: Natural -> N
nFromNatural 0 = Z
nFromNatural x = S (nFromNatural (x - 1))

nToNum :: (Num a) => N -> a
nToNum Z = 0
nToNum (S x) = 1 + nToNum x

nEven :: N -> Bool
nEven Z = True
nEven (S x) = not (nEven x)

nOdd :: N -> Bool
nOdd x = not (nEven x)

ndivmod :: N -> N -> (N, N)
ndivmod _ Z = throw DivideByZero
ndivmod x y = case ncmp x y of
  LT -> (Z, x)
  _ -> (S division, remain)
  where
    (Just x') = nsub x y
    (division, remain) = ndivmod x' y

ndiv :: N -> N -> N
ndiv x y = division
  where
    (division, _) = ndivmod x y

nmod :: N -> N -> N
nmod x y = remain
  where
    (_, remain) = ndivmod x y
