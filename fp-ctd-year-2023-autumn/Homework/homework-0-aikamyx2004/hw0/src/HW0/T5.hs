module HW0.T5
  ( Nat
  , nFromNatural
  , nmult
  , nplus
  , ns
  , nToNum
  , nz
  ) where

import Numeric.Natural

type Nat a = (a -> a) -> a -> a

nz :: Nat a
nz _ z = z

ns :: Nat a -> Nat a
ns n f x = f (n f x)

nplus :: Nat a -> Nat a -> Nat a
nplus a b f x = a f (b f x)

nmult :: Nat a -> Nat a -> Nat a
nmult a b f = a (b f)

nFromNatural :: Natural -> Nat a
nFromNatural 0 _ x = x
nFromNatural n f x = f (nFromNatural (n - 1) f x)

nToNum :: Num a => Nat a -> a
nToNum n = n (+1) 0
