/// ABBYY® Mobile Capture © 2019 ABBYY Production LLC.
/// ABBYY is a registered trademark or a trademark of ABBYY Software Ltd.

#import "NSDictionary+parseReactTypes.h"

@implementation NSDictionary (rtr_parseReactTypes)

- (int)rtr_parseReactInt:(NSString*)key defaultValue:(int)defaultValue error:(NSError**)error
{
	NSNumber* number = self[key];
	if(number != nil && ![self rtr_checkValue:number forKey:key isKindOfClass:NSNumber.class error:error]) {
		return -1;
	}
	return number ? number.intValue : defaultValue;
}
- (BOOL)rtr_parseReactBool:(NSString*)key defaultValue:(BOOL)defaultValue error:(NSError**)error
{
	NSNumber* number = self[key];
	if(number != nil && ![self rtr_checkValue:number forKey:key isKindOfClass:NSNumber.class error:error]) {
		return NO;
	}
	return number ? number.boolValue : defaultValue;
}

- (float)rtr_parseReactFloat:(NSString*)key defaultValue:(float)defaultValue error:(NSError**)error
{
	NSNumber* number = self[key];
	if(number != nil && ![self rtr_checkValue:number forKey:key isKindOfClass:NSNumber.class error:error]) {
		return NAN;
	}
	return number ? number.floatValue : defaultValue;
}

- (BOOL)rtr_checkValue:(id)value forKey:(NSString*)key isKindOfClass:(Class)class error:(NSError**)error
{
	if([value isKindOfClass:class]) {
		return YES;
	}
	if(error != nil) {
		NSString* description = [NSString stringWithFormat:@"Value '%@' for key '%@' is not of class '%@'", value, key, NSStringFromClass(class)];
		NSDictionary* userInfo = @{NSLocalizedDescriptionKey: description};
		*error = [NSError errorWithDomain:@"ABBYY" code:-1 userInfo:userInfo];
	}
	return NO;
}

- (NSInteger)rtr_parseReactEnum:(NSString*)key variants:(NSArray<NSString*>*)variants defaultValue:(NSInteger)defaultValue error:(NSError**)error
{
	NSString* value = self[key];

	if(value == nil) {
		return defaultValue;
	}

	if(![self rtr_checkValue:value forKey:key isKindOfClass:NSString.class error:error]) {
		return NSNotFound;
	}

	NSInteger index = NSNotFound;

	for(int i = 0; i < variants.count; i++) {
		NSString* variant = variants[i];
		if([variant compare:value options:NSCaseInsensitiveSearch] == NSOrderedSame) {
			index = i;
			break;
		}
	}

	if(error != nil && index == NSNotFound) {
		NSString* description = [NSString stringWithFormat:@"Failed to parse value '%@' for key '%@'", value, key];
		NSDictionary* userInfo = @{NSLocalizedDescriptionKey: description};
		*error = [NSError errorWithDomain:@"ABBYY" code:-1 userInfo:userInfo];
	}

	return index;
}


@end
