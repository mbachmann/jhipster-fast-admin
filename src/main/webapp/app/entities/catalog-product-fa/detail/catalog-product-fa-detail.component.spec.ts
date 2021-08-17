import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CatalogProductFaDetailComponent } from './catalog-product-fa-detail.component';

describe('Component Tests', () => {
  describe('CatalogProductFa Management Detail Component', () => {
    let comp: CatalogProductFaDetailComponent;
    let fixture: ComponentFixture<CatalogProductFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CatalogProductFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ catalogProduct: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CatalogProductFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CatalogProductFaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load catalogProduct on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.catalogProduct).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
