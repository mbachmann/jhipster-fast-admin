import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CatalogUnitDetailComponent } from './catalog-unit-detail.component';

describe('Component Tests', () => {
  describe('CatalogUnit Management Detail Component', () => {
    let comp: CatalogUnitDetailComponent;
    let fixture: ComponentFixture<CatalogUnitDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CatalogUnitDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ catalogUnit: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CatalogUnitDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CatalogUnitDetailComponent);
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
