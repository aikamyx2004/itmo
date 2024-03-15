{-# LANGUAGE DataKinds            #-}
{-# LANGUAGE TypeFamilies         #-}
{-# LANGUAGE TypeOperators        #-}
{-# LANGUAGE UndecidableInstances #-}

module HW6.T2
  ( TSet
  , Contains
  , Add
  , Delete
  ) where

import Data.Type.Bool (If)
import GHC.TypeLits

type TSet = [Symbol]

type family Contains (name :: Symbol) (set :: TSet) :: Bool where
  Contains name '[]         = 'False
  Contains name (name ': _) = 'True
  Contains name (_ ': rest) = Contains name rest

type family Delete (name :: Symbol) (set :: TSet) :: TSet where
  Delete name '[]            = '[]
  Delete name (name ': rest) = rest
  Delete name (head ': rest) = head ': Delete name rest

type family Add (name :: Symbol) (set :: TSet) :: TSet where
  Add name set = If (Contains name set) set (name ': set)
