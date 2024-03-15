{-# LANGUAGE FlexibleContexts #-}

module HW6.T1
  ( BucketsArray
  , CHT (..)

  , newCHT
  , getCHT
  , putCHT
  , sizeCHT

  , initCapacity
  , loadFactor
  ) where

import Control.Concurrent.Classy (MonadConc, STM, atomically, newTVar, readTVar, readTVarConc,
                                  writeTVar)
import Control.Concurrent.Classy.STM (TArray, TVar)
import Data.Array.Base (MArray (getNumElements))
import Data.Array.MArray (newArray, readArray, writeArray)
import Data.Foldable (find)
import Data.Hashable

initCapacity :: Int
initCapacity = 16

loadFactor :: Double
loadFactor = 0.75

type Bucket k v = [(k, v)]
type BucketsArray stm k v = TArray stm Int (Bucket k v)

data CHT stm k v = CHT
  { chtBuckets :: TVar stm (BucketsArray stm k v)
  , chtSize    :: TVar stm Int
  }

newCHT :: (MonadConc m) => m (CHT (STM m) k v)
newCHT = atomically $ do
  arr <- newArray (0, initCapacity) []
  size <- newTVar 0
  buckets <- newTVar arr
  return (CHT buckets size)


getCHT
  :: (MonadConc m, Eq k, Hashable k)
  => k
  -> CHT (STM m) k v
  -> m (Maybe v)
getCHT key (CHT chtBucketsVar _) = atomically $ do
  -- get bucket
  bucketsVar <- readTVar chtBucketsVar
  capacity <- getNumElements bucketsVar
  let idx = getIdx key capacity
  bucket <- readArray bucketsVar idx

  -- get value or nothing
  return $ snd <$> find (\p -> fst p == key) bucket

putCHT
  :: (MonadConc m, Eq k, Hashable k)
  => k
  -> v
  -> CHT (STM m) k v
  -> m ()
putCHT key value (CHT chtBucketsVar sizeVar) = atomically $ do
  -- get bucket
  bucketsT <- readTVar chtBucketsVar
  capacity <- getNumElements bucketsT
  let idx = getIdx key capacity
  curBucket <- readArray bucketsT idx

  -- put
  let updatedBucket = addToBucket key value curBucket

  -- change bucket
  writeArray bucketsT idx updatedBucket
  writeTVar chtBucketsVar bucketsT

  -- change size
  size <- readTVar sizeVar
  let newSize = size + length updatedBucket - length curBucket
  writeTVar sizeVar newSize


addToBucket :: Eq k => k -> v -> [(k, v)] -> [(k, v)]
addToBucket key value [] = [(key, value)]
addToBucket key value ((k, v):xs)
  | key == k = (k, value) : xs
  | otherwise = (k, v):addToBucket key value xs

sizeCHT :: MonadConc m => CHT (STM m) k v -> m Int
sizeCHT (CHT _ sizeVar) = readTVarConc sizeVar

getIdx :: (Hashable k) => k -> Int -> Int
getIdx key capacity = hash key `mod` capacity
