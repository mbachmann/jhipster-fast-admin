import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICountryMySuffix, CountryMySuffix } from '../country-my-suffix.model';
import { CountryMySuffixService } from '../service/country-my-suffix.service';
import { ILocationMySuffix } from 'app/entities/location-my-suffix/location-my-suffix.model';
import { LocationMySuffixService } from 'app/entities/location-my-suffix/service/location-my-suffix.service';

@Component({
  selector: 'jhl-country-my-suffix-update',
  templateUrl: './country-my-suffix-update.component.html',
})
export class CountryMySuffixUpdateComponent implements OnInit {
  isSaving = false;

  locationsSharedCollection: ILocationMySuffix[] = [];

  editForm = this.fb.group({
    id: [],
    countryName: [],
    location: [],
  });

  constructor(
    protected countryService: CountryMySuffixService,
    protected locationService: LocationMySuffixService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ country }) => {
      this.updateForm(country);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const country = this.createFromForm();
    if (country.id !== undefined) {
      this.subscribeToSaveResponse(this.countryService.update(country));
    } else {
      this.subscribeToSaveResponse(this.countryService.create(country));
    }
  }

  trackLocationMySuffixById(index: number, item: ILocationMySuffix): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICountryMySuffix>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(country: ICountryMySuffix): void {
    this.editForm.patchValue({
      id: country.id,
      countryName: country.countryName,
      location: country.location,
    });

    this.locationsSharedCollection = this.locationService.addLocationMySuffixToCollectionIfMissing(
      this.locationsSharedCollection,
      country.location
    );
  }

  protected loadRelationshipsOptions(): void {
    this.locationService
      .query()
      .pipe(map((res: HttpResponse<ILocationMySuffix[]>) => res.body ?? []))
      .pipe(
        map((locations: ILocationMySuffix[]) =>
          this.locationService.addLocationMySuffixToCollectionIfMissing(locations, this.editForm.get('location')!.value)
        )
      )
      .subscribe((locations: ILocationMySuffix[]) => (this.locationsSharedCollection = locations));
  }

  protected createFromForm(): ICountryMySuffix {
    return {
      ...new CountryMySuffix(),
      id: this.editForm.get(['id'])!.value,
      countryName: this.editForm.get(['countryName'])!.value,
      location: this.editForm.get(['location'])!.value,
    };
  }
}
