import { ICountryMySuffix } from 'app/entities/country-my-suffix/country-my-suffix.model';

export interface IRegionMySuffix {
  id?: number;
  regionName?: string | null;
  country?: ICountryMySuffix | null;
}

export class RegionMySuffix implements IRegionMySuffix {
  constructor(public id?: number, public regionName?: string | null, public country?: ICountryMySuffix | null) {}
}

export function getRegionMySuffixIdentifier(region: IRegionMySuffix): number | undefined {
  return region.id;
}
