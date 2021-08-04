import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IRegionMySuffix, RegionMySuffix } from '../region-my-suffix.model';
import { RegionMySuffixService } from '../service/region-my-suffix.service';
import { ICountryMySuffix } from 'app/entities/country-my-suffix/country-my-suffix.model';
import { CountryMySuffixService } from 'app/entities/country-my-suffix/service/country-my-suffix.service';

@Component({
  selector: 'jhl-region-my-suffix-update',
  templateUrl: './region-my-suffix-update.component.html',
})
export class RegionMySuffixUpdateComponent implements OnInit {
  isSaving = false;

  countriesSharedCollection: ICountryMySuffix[] = [];

  editForm = this.fb.group({
    id: [],
    regionName: [],
    country: [],
  });

  constructor(
    protected regionService: RegionMySuffixService,
    protected countryService: CountryMySuffixService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ region }) => {
      this.updateForm(region);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const region = this.createFromForm();
    if (region.id !== undefined) {
      this.subscribeToSaveResponse(this.regionService.update(region));
    } else {
      this.subscribeToSaveResponse(this.regionService.create(region));
    }
  }

  trackCountryMySuffixById(index: number, item: ICountryMySuffix): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRegionMySuffix>>): void {
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

  protected updateForm(region: IRegionMySuffix): void {
    this.editForm.patchValue({
      id: region.id,
      regionName: region.regionName,
      country: region.country,
    });

    this.countriesSharedCollection = this.countryService.addCountryMySuffixToCollectionIfMissing(
      this.countriesSharedCollection,
      region.country
    );
  }

  protected loadRelationshipsOptions(): void {
    this.countryService
      .query()
      .pipe(map((res: HttpResponse<ICountryMySuffix[]>) => res.body ?? []))
      .pipe(
        map((countries: ICountryMySuffix[]) =>
          this.countryService.addCountryMySuffixToCollectionIfMissing(countries, this.editForm.get('country')!.value)
        )
      )
      .subscribe((countries: ICountryMySuffix[]) => (this.countriesSharedCollection = countries));
  }

  protected createFromForm(): IRegionMySuffix {
    return {
      ...new RegionMySuffix(),
      id: this.editForm.get(['id'])!.value,
      regionName: this.editForm.get(['regionName'])!.value,
      country: this.editForm.get(['country'])!.value,
    };
  }
}
