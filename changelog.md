- Fixed not being able to unlock the Totempedia entries in the Music Instruments category.
  
  Should an entry still be locked even though you have the instrument's crafting recipe, make sure that you also have the corresponding advancement, e.g. by using the command `/advancement grant <username> only totemic:recipes/misc/<instrument>`, or by placing a block of Red Cedar Leaves, a Buffalo Tooth, a Red Cedar Log or an Eagle Bone in your inventory, depending on the instrument.
- Internal changes to how the code is organized

API changes:
- Added new constructors to the Ceremony class which take two MusicInstruments (or Suppliers thereof) as arguments, rather than varargs. The varargs constructor has been deprecated.
- Added new constructors to (Portable)TotemCarving which take only one effect. The varargs constructor stays and is not deprecated.
- Added new constructors to PotionTotemEffect which take the MobEffect arguments directly rather than as Suppliers. The Supplier taking constructors have been deprecated.
- PotionTotemEffect is no longer storing the MobEffect as a Supplier, which means that the MobEffect has to exist at the time the PotionTotemEffect is constructed, possibly making it necessary to use a DeferredRegister or similar for the corresponding carving.
