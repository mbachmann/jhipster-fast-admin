import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CatalogCategoryFaService } from '../service/catalog-category-fa.service';

import { CatalogCategoryFaComponent } from './catalog-category-fa.component';

describe('Component Tests', () => {
  describe('CatalogCategoryFa Management Component', () => {
    let comp: CatalogCategoryFaComponent;
    let fixture: ComponentFixture<CatalogCategoryFaComponent>;
    let service: CatalogCategoryFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CatalogCategoryFaComponent],
      })
        .overrideTemplate(CatalogCategoryFaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CatalogCategoryFaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CatalogCategoryFaService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.catalogCategories?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
