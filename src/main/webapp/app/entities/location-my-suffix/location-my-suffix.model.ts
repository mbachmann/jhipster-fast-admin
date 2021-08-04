import { ICountryMySuffix } from 'app/entities/country-my-suffix/country-my-suffix.model';
import { IDepartmentMySuffix } from 'app/entities/department-my-suffix/department-my-suffix.model';

export interface ILocationMySuffix {
  id?: number;
  streetAddress?: string | null;
  postalCode?: string | null;
  city?: string | null;
  stateProvince?: string | null;
  countries?: ICountryMySuffix[] | null;
  department?: IDepartmentMySuffix | null;
}

export class LocationMySuffix implements ILocationMySuffix {
  constructor(
    public id?: number,
    public streetAddress?: string | null,
    public postalCode?: string | null,
    public city?: string | null,
    public stateProvince?: string | null,
    public countries?: ICountryMySuffix[] | null,
    public department?: IDepartmentMySuffix | null
  ) {}
}

export function getLocationMySuffixIdentifier(location: ILocationMySuffix): number | undefined {
  return location.id;
}
