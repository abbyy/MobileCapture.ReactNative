/// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
/// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NSDictionary (rtr_parseReactTypes)

- (int)rtr_parseReactInt:(NSString*)key defaultValue:(int)defaultValue error:( NSError* _Nullable *)error;
- (BOOL)rtr_parseReactBool:(NSString*)key defaultValue:(BOOL)defaultValue error:( NSError* _Nullable *)error;
- (float)rtr_parseReactFloat:(NSString*)key defaultValue:(float)defaultValue error:( NSError* _Nullable *)error;
- (NSInteger)rtr_parseReactEnum:(NSString*)key variants:(NSArray<NSString*>*)variants defaultValue:(NSInteger)defaultValue error:( NSError* _Nullable *)error;

@end

NS_ASSUME_NONNULL_END
