jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OwnerMySuffixService } from '../service/owner-my-suffix.service';

import { OwnerMySuffixComponent } from './owner-my-suffix.component';

describe('Component Tests', () => {
  describe('OwnerMySuffix Management Component', () => {
    let comp: OwnerMySuffixComponent;
    let fixture: ComponentFixture<OwnerMySuffixComponent>;
    let service: OwnerMySuffixService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OwnerMySuffixComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { snapshot: { queryParams: {} } },
          },
        ],
      })
        .overrideTemplate(OwnerMySuffixComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OwnerMySuffixComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(OwnerMySuffixService);

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
      expect(comp.owners?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
