import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CatalogUnitFaDetailComponent } from './catalog-unit-fa-detail.component';

describe('Component Tests', () => {
  describe('CatalogUnitFa Management Detail Component', () => {
    let comp: CatalogUnitFaDetailComponent;
    let fixture: ComponentFixture<CatalogUnitFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CatalogUnitFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ catalogUnit: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CatalogUnitFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CatalogUnitFaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load catalogUnit on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.catalogUnit).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
