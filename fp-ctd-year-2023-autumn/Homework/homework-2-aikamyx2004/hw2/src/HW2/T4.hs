module HW2.T4
  ( DotString (..)
  , Fun (..)
  , Inclusive (..)
  , ListPlus (..)
  ) where

data ListPlus a = a :+ ListPlus a | Last a
  deriving Show

infixr 5 :+

instance Semigroup (ListPlus a) where
  (<>) = undefined

data Inclusive a b = This a | That b | Both a b
  deriving Show

-- You may necessary constraints there
instance Semigroup (Inclusive a b) where
  (<>) = undefined

newtype DotString = DS String
  deriving Show

instance Semigroup DotString where
  (<>) = undefined

instance Monoid DotString where
  mempty = undefined

newtype Fun a = F (a -> a)

instance Semigroup (Fun a) where
  (<>) = undefined

instance Monoid (Fun a) where
  mempty = undefined
