import { IRegionMySuffix } from 'app/entities/region-my-suffix/region-my-suffix.model';
import { ILocationMySuffix } from 'app/entities/location-my-suffix/location-my-suffix.model';

export interface ICountryMySuffix {
  id?: number;
  countryName?: string | null;
  regions?: IRegionMySuffix[] | null;
  location?: ILocationMySuffix | null;
}

export class CountryMySuffix implements ICountryMySuffix {
  constructor(
    public id?: number,
    public countryName?: string | null,
    public regions?: IRegionMySuffix[] | null,
    public location?: ILocationMySuffix | null
  ) {}
}

export function getCountryMySuffixIdentifier(country: ICountryMySuffix): number | undefined {
  return country.id;
}
